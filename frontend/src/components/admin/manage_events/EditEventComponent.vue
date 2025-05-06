<script lang="ts" setup>
import { useEventStore } from '@/stores/eventStore';
import { onMounted, ref, watch } from 'vue';
import { eventIcons } from '@/utils/icons';
import { formatDate, formatDateForInput } from '@/utils/formatDate';
import { validateIconType, validateLatitude, validateLongitude, validatePointDescription, validateRadius, validateSeverity } from '@/utils/validationService';

// Store imports
const eventStore = useEventStore();

// Props
const localEvent = ref({
  id: 0,
  name: '',
  description: '',
  iconType: '',
  startTime: '',
  endTime: '',
  latitude: 0,
  longitude: 0,
  radius: 0,
  severity: 0,
});
const isEventDirty = ref(false);
const selectedIcon = ref('none');
const icons = eventIcons;

// Load events from store
const loadChosenEvent = async () => {
    await eventStore.fetchChosenEvent();
    if(!eventStore.chosenEvent.endTime) {
        localEvent.value = {
            ...eventStore.chosenEvent,
            startTime: formatDateForInput(eventStore.chosenEvent.startTime)
    }
    } else {
        localEvent.value = {
            ...eventStore.chosenEvent,
            startTime: formatDateForInput(eventStore.chosenEvent.startTime),
            endTime: formatDateForInput(eventStore.chosenEvent.endTime),
        }
    }
}

onMounted( async () => {
    await loadChosenEvent();
});

// Watch for changes in the chosen event
watch(() => eventStore.chosenEvent, async (newEvent) => {
    console.log("startTime from newEvent:", newEvent.startTime);

    if(newEvent) {
        localEvent.value = newEvent;
        selectedIcon.value = newEvent.iconType;
        console.log(localEvent.value);
    }
});

// Validate new event data
const validateEvent = () => {
    if (!validatePointDescription(localEvent.value.description)) {
        alert('Fyll inn en gyldig beskrivelse');
        return false;
    }
    if (!localEvent.value.startTime) {
        alert('Startdato må fylles ut');
        return false;
    }
    if (!validateLatitude(localEvent.value.latitude)) {
        alert('Fyll inn en gyldig breddegrad');
        return false;
    }
    if (!validateLongitude(localEvent.value.longitude)) {
        alert('Fyll inn en gyldig lengdegrad');
        return false;
    }
    if (!validateRadius(localEvent.value.radius)) {
        alert('Fyll inn en gyldig radius');
        return false;
    }
    if (!validateSeverity(localEvent.value.severity)) {
        alert('Fyll inn et gyldig krisenivå');
        return false;
    }
    if (!validateIconType(localEvent.value.iconType)) {
        alert('Velg et ikon');
        return false;
    }
    if (localEvent.value.endTime && new Date(localEvent.value.startTime) > new Date(localEvent.value.endTime)) {
        alert('Sluttdato kan ikke være før startdato');
        return false;
    }
    return true;
}

// Update event
const updateEvent = async () => {
    if (!validateEvent()) {
        return;
    }
    const formattedStartTime = formatDate(new Date(localEvent.value.startTime));
    const formattedEndTime = localEvent.value.endTime
        ? formatDate(new Date(localEvent.value.endTime))
        : null;
    if(formattedEndTime) {
        await eventStore.update(localEvent.value.id, {
            name: localEvent.value.name,
            description: localEvent.value.description,
            iconType: selectedIcon.value,
            startTime: formattedStartTime,
            endTime: formattedEndTime,
            latitude: localEvent.value.latitude,
            longitude: localEvent.value.longitude,
            radius: localEvent.value.radius,
            severity: localEvent.value.severity
        });
    } else {
        await eventStore.update(localEvent.value.id, {
            name: localEvent.value.name,
            description: localEvent.value.description,
            iconType: selectedIcon.value,
            startTime: formattedStartTime,
            latitude: localEvent.value.latitude,
            longitude: localEvent.value.longitude,
            radius: localEvent.value.radius,
            severity: localEvent.value.severity
        });
    }
    
    isEventDirty.value = false;
}

// Delete event
const deleteEvent = async () => {
    if (!confirm('Er du sikker på at du vil slette hendelsen?')) {
        return;
    }
    await eventStore.delete(localEvent.value.id);
}
</script>
<template>
<div class="page-container">
    <div class="header-box-container">
        <h1 class="medium-header">Rediger hendelse</h1>
        <div class="grey-container">
            <h2 class="small-header">{{ localEvent.name }}</h2>
            
                <!-- Severity and radius -->
                <div class="double-label-container">
                    <label for="severity-input">Krisenivå</label>
                    <label for="radius-input">Radius</label>
                </div>
                <div class="double-input-container">                   
                    <input type="text" class="edit-input" id="severity-input" @input="isEventDirty = true" v-model="localEvent.severity" />                    
                    <input type="text" class="edit-input" id="radius-input" @input="isEventDirty = true" v-model="localEvent.radius" />
                </div>

                <!-- Coordinates -->
                <label for="coordinate-input">Koordinater</label>
                <div class="double-input-container">
                    <input type="text" class="edit-input" id="coordinate-input" @input="isEventDirty = true" v-model="localEvent.latitude" />
                    <input type="text" class="edit-input" id="coordinate-input" @input="isEventDirty = true" v-model="localEvent.longitude" />
                </div>

                <!-- Description and dates -->
                <label for="description-input">Beskrivelse</label>
                <textarea class="edit-input" id="description-input" @input="isEventDirty = true" v-model="localEvent.description"></textarea>
                <label for="start-input">Startdato</label>
                <input type="datetime-local" class="edit-input" id="start-input" @input="isEventDirty = true" v-model="localEvent.startTime" />
                <label for="end-input">Eventuell sluttdato</label>
                <input type="datetime-local" class="edit-input" id="end-input" @input="isEventDirty = true" v-model="localEvent.endTime" />
                
                <!-- Icon type -->
                <select id="icon-select" v-model="selectedIcon" class="edit-input">
                    <option disabled value="">Velg ikon</option>
                    <option v-for="(icon, index) in icons" :key="index" :value="icon">
                      {{ icon }}
                    </option>
                </select>
                
            <div class="button-container">
                <button class="dark-button" id="delete" @click="deleteEvent()">Slett hendelse</button>
                <button class="dark-button" id="save" @click="updateEvent()" :disabled="!isEventDirty">Lagre endringer</button>
            </div>
        </div>
    </div>
</div>
</template>
<style scoped>
    .grey-container {
        background-color: var(--light-blue);
        max-height: 42rem;
    }

    .page-container {
        margin: 1rem;
        display: flex;
        flex-direction: column;
    }

    .small-header {
        color: white;
    }

    label {
        color: white;
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

    select {
        font-size: var(--font-size-medium);
    }

    .button-container {
        display: flex;
        justify-content: space-between;
        margin-top: 1rem;
    }

    .dark-button {
        height: 3rem;
        width: 10rem;
    }

    #delete:hover {
        background-color: var(--bad-red);
    }

    #save:hover {
        background-color: var(--good-green);
    }

    #save:disabled {
        opacity: 0.5;
    }
</style>