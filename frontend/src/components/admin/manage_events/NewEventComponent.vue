<script lang="ts" setup>
import { ref, watch } from 'vue';
import { eventIcons } from '@/utils/values';
import { validateLatitude, validateLongitude, validatePointDescription, validatePointName, validateRadius, validateSeverity } from '@/utils/validationService';
import { useEventStore } from '@/stores/eventStore';
import { severityLevels } from '@/utils/values';

const props = defineProps<{
  lat: number | null;
  lng: number | null;
}>();

// Props
const name = ref('');
const severity = ref();
const radius = ref();
const latitude = ref(props.lat ?? '');
const longitude = ref(props.lng ?? '');
const description = ref('');
const startTime = ref();
const endTime = ref();
const selectedIcon = ref('none');
const emit = defineEmits(['hide-new-event-box', 'new-event-success']);
const icons = eventIcons;
const eventStore = useEventStore();
const errorMsg = ref('');
const response = ref('');

const validateEvent = () => {
    errorMsg.value = '';
    if (!validatePointName(name.value)) {
        errorMsg.value = ('Hendelsesnavn må fylles ut');
        return false;
    }
    if (!validateSeverity(severity.value)) {
        errorMsg.value = ('Krisenivå må fylles ut');
        return false;
    }
    if (!validateRadius(radius.value)) {
        errorMsg.value = ('Radius må fylles ut');
        return false;
    }
    if (!validateLatitude(Number(latitude.value))) {
        errorMsg.value = ('Lengdegrad må fylles ut');
        return false;
    }
    if (!validateLongitude(Number(longitude.value))) {
        errorMsg.value = ('Breddegrad må fylles ut');
        return false;
    }
    if (!validatePointDescription(description.value)) {
        errorMsg.value = ('Beskrivelse må fylles ut');
        return false;
    }
    if (!startTime.value) {
        errorMsg.value = ('Startdato må fylles ut');
        return false;
    }
    if (endTime.value && new Date(startTime.value) > new Date(endTime.value)) {
        errorMsg.value = ('Sluttdato kan ikke være før startdato');
        return false;
    }
    return true;
}

// Add event 
const addEvent = async () => {
    if (!validateEvent()) {
        return;
    }
    if (endTime.value) {
        await eventStore.save({
            name: name.value,
            description: description.value,
            iconType: selectedIcon.value,
            startTime: startTime.value + ':00',
            endTime: endTime.value + ':00',
            latitude: Number(latitude.value),
            longitude: Number(longitude.value),
            severity: severity.value,
            radius: radius.value,
        }).catch((error) => {
            response.value = error;
            $toast.error('Feil ved opprettelse av bruker!' + error, {
                duration: 3000,
                position: 'top-right'
            });
        });
    } else {
        eventStore.save({
            name: name.value,
            description: description.value,
            iconType: selectedIcon.value,
            startTime: startTime.value + ':00',
            latitude: Number(latitude.value),
            longitude: Number(longitude.value),
            severity: severity.value,
            radius: radius.value,
        }).catch((error) => {
            response.value = error;
            $toast.error('Feil ved opprettelse av bruker!' + error, {
                duration: 3000,
                position: 'top-right'
            });
        });
    }
    if (response.value) {
        $toast.error('Feil ved opprettelse av hendelse!' + response.value, {
            duration: 3000,
            position: 'top-right'
        });
    } else {
        emit('new-event-success');
    }
    
    
}

</script>
<template>
    <div class="grey-container">
        <div class="header-container">
            <h1 class="medium-header">Ny hendelse</h1>
            <button class="cancel-button" @click="$emit('hide-new-event-box')">X</button> 
        </div>

        <div class="item-input">
            <label for="name-input">*Hendelsesnavn</label>
            <input type="text" class="edit-input" id="name-input" v-model="name" />                    

            <!-- Severity and radius -->
            <div class="double-label-container">
                <label for="severity-input">*Krisenivå</label>
                <label for="radius-input" id="radius-label">*Radius (meter)</label>
            </div>
            <div class="double-input-container">     
                <select v-model="severity" id="severity-input" class="dropdown-select">
                    <option class="edit-input" v-for="level in severityLevels" :key="level" :value="level">{{ level }}</option>
                </select>           
                <input type="text" class="edit-input" v-model="radius" />
            </div>

            <!-- Coordinates -->
             <div class="double-label-container">
                <label for="coordinate-input">*Breddegrad</label>
                <label for="coordinate-input">*Lengdegrad</label>
            </div>
            <div class="double-input-container">
                <input type="text" class="edit-input" id="coordinate-input" placeholder="f.eks 62.1008" v-model="latitude" />
                <input type="text" class="edit-input" id="coordinate-input" placeholder="f.eks 10.1199" v-model="longitude" />
            </div>

            <!-- Description and dates -->
            <label for="description">*Beskrivelse</label>
            <textarea class="edit-input" id="description" placeholder="Minst 5 tegn" v-model="description"></textarea>
            <label for="start-input">*Startdato</label>
            <input type="datetime-local" class="edit-input" id="start-input" v-model="startTime" />   
            <label for="end-input">Eventuell Sluttdato</label>
            <input type="datetime-local" class="edit-input" id="end-input" v-model="endTime" />       
            
            
            <!-- Icon type -->
            <label for="icon-select">*Velg ikon</label>
            <select id="icon-select" v-model="selectedIcon" class="edit-input">
                <option v-for="(icon, index) in icons" :key="index" :value="icon">
                  {{ icon }}
                </option>
            </select>
        </div>

        <div class="button-container">
            <button class="dark-button" @click="addEvent">Legg til</button>
        </div>
        <p class="error-message">{{ errorMsg }}</p>
    </div>
</template>
<style scoped>
.grey-container {
   height:41rem;
   margin-top: 4.75rem;
}

.header-container {
    position: relative;
    margin-top: 1rem;
}

.double-label-container, .double-input-container {
    display: flex;
    flex-direction: row;
}

.double-label-container {
    gap: 6rem;
}

#radius-label {
    margin-left: 1rem;
}

.double-input-container {
    justify-content: space-between;
    gap: 1rem;
    width: 22.85rem;
}

.medium-header {
    margin: 0;
}

.edit-input {
    background-color: white; 
    margin-right: 1rem;
    max-width: 24rem;
}

textarea {
    width: 100%;
    height: 2.5rem;
    padding: 0.5rem;
    margin-bottom: 1rem;
    border: none;
    border-radius: 5px;
    background-color: var(--grey);
    font-size: 1rem;
    box-sizing: border-box;
}

.button-container {
    display: flex;
    justify-content: space-between;
    margin: 0;
}

.dark-button {
    width: 10rem; 
    height: 3rem; 
    background-color: var(--good-green);
}

.cancel-button {
    position:absolute;
    top: -1rem;
    right: -10rem;
    color: var(--bad-red);
    background-color: transparent;
}

.dropdown-container {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    width: 200px;
}
  
.dropdown-select {
    padding: 0.5rem;
    border-radius: 4px;
    background-color: white;
    width: 22rem;
    margin-right: 1rem;
}

p {
    font-size: var(--font-size-medium);
    color: var(--bad-red);
    margin-top: 0.5rem;
}
</style>