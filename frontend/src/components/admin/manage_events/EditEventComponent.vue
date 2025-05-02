<script lang="ts" setup>
import { useEventStore, type CrisisEvent } from '@/stores/eventStore';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';

// Store imports
const eventStore = useEventStore();

// Props
const localEvent = ref({ ...eventStore.event } as CrisisEvent);
const isEventDirty = ref(false);
const selectedIcon = ref('none');
const icons = [
    'none', 
    'point', 
    'normal',
    'danger',
    'assembly_point',
    'medical',
    'shelter'
];

onMounted(() => {
    if (eventStore.event) {
        localEvent.value = { ...eventStore.event };
    }
});

// Update event
const updateEvent = async () => {
    // TODO
    isEventDirty.value = false;
}
// Delete event
const deleteEvent = async () => {
    // TODO
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
                <input type="date" class="edit-input" id="start-input" @input="isEventDirty = true" v-model="localEvent.startDate" />
                <label for="end-input">Eventuell sluttdato</label>
                <input type="date" class="edit-input" id="end-input" @input="isEventDirty = true" v-model="localEvent.endDate" />
                
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
        height: 40rem;
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