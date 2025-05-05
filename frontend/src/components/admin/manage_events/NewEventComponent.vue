<script lang="ts" setup>
import { ref } from 'vue';
import { eventIcons } from '@/utils/icons';
import { validateLatitude, validateLongitude, validatePointDescription, validatePointName, validateRadius, validateSeverity } from '@/utils/validationService';
import { useEventStore } from '@/stores/eventStore';

// Props
const name = ref('');
const severity = ref();
const radius = ref();
const latitude = ref();
const longitude = ref();
const description = ref('');
const startTime = ref();
const endTime = ref();
const selectedIcon = ref('none');
const emit = defineEmits(['hide-new-event-box']);
const icons = eventIcons;
const eventStore = useEventStore();

const validateEvent = () => {
    if (!validatePointName(name.value)) {
        alert('Hendelsesnavn må fylles ut');
        return false;
    }
    if (!validateSeverity(severity.value)) {
        alert('Krisenivå må fylles ut');
        return false;
    }
    if (!validateRadius(radius.value)) {
        alert('Radius må fylles ut');
        return false;
    }
    if (!validateLatitude(latitude.value)) {
        alert('Lengdegrad må fylles ut');
        return false;
    }
    if (!validateLongitude(longitude.value)) {
        alert('Breddegrad må fylles ut');
        return false;
    }
    if (!validatePointDescription(description.value)) {
        alert('Beskrivelse må fylles ut');
        return false;
    }
    if (!startTime.value) {
        alert('Startdato må fylles ut');
        return false;
    }
    if (endTime.value && new Date(startTime.value) > new Date(endTime.value)) {
        alert('Sluttdato kan ikke være før startdato');
        return false;
    }
    return true;
}

// Add event 
const addEvent = () => {
    if (validateEvent()) {
        if (endTime.value) {
            eventStore.save({
                name: name.value,
                description: description.value,
                iconType: selectedIcon.value,
                startTime: startTime.value + ':00',
                endTime: endTime.value + ':00',
                latitude: latitude.value,
                longitude: longitude.value,
                severity: severity.value,
                radius: radius.value,
            });
        } else {
            eventStore.save({
                name: name.value,
                description: description.value,
                iconType: selectedIcon.value,
                startTime: startTime.value + ':00',
                latitude: latitude.value,
                longitude: longitude.value,
                severity: severity.value,
                radius: radius.value,
            });
        }
        emit('hide-new-event-box');
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
            <input type="text" class="edit-input" placeholder="*Hendelses-navn" v-model="name" />                    

            <!-- Severity and radius -->
            <div class="double-input-container">                   
                <input type="text" class="edit-input" placeholder="*Krisenivå" v-model="severity" />                    
                <input type="text" class="edit-input" placeholder="*Radius" v-model="radius" />
            </div>

            <!-- Coordinates -->
            <div class="double-input-container">
                <input type="text" class="edit-input" placeholder="*Lengdegrad" v-model="latitude" />
                <input type="text" class="edit-input" placeholder="*Breddegrad" v-model="longitude" />
            </div>

            <!-- Description and dates -->
            <textarea class="edit-input" placeholder="*Beskrivelse" v-model="description"></textarea>
            <div class="double-label-container">
                <label for="start-input">*Startdato</label>
                <label for="end-input">Eventuell Sluttdato</label>
            </div>
            <div class="double-input-container">
                <input type="datetime-local" class="edit-input" id="start-input" v-model="startTime" />    
                <input type="datetime-local" class="edit-input" id="end-input" v-model="endTime" />       
            </div>
            
            <!-- Icon type -->
            <select id="icon-select" v-model="selectedIcon" class="edit-input">
                <option disabled value="">Velg ikon</option>
                <option v-for="(icon, index) in icons" :key="index" :value="icon">
                  {{ icon }}
                </option>
            </select>
        </div>

        <div class="button-container">
            <button class="dark-button" @click="addEvent">Legg til</button>
        </div>
    </div>
</template>
<style scoped>
.grey-container {
   height: 30rem;
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
    gap: 9rem;
}

.double-input-container {
    justify-content: space-between;
    gap: 1rem;
}

.medium-header {
    margin: 0.5rem 0 1rem 0;
}

.edit-input {
    background-color: white; 
    margin-right: 1rem;
    max-width: 24rem;
   
}

textarea {
    width: 100%;
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
}

.dark-button {
    width: 10rem; 
    height: 3rem; 
    background-color: var(--orange);
}

.cancel-button {
    position:absolute;
    top: -0.5rem;
    right: -9.5rem;
    color: var(--bad-red);
    background-color: transparent;
}
</style>