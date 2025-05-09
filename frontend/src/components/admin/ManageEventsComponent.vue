<script lang="ts" setup>
import { ref, watch } from 'vue';
import EditEventComponent from './manage_events/EditEventComponent.vue';
import ViewEventsComponent from './manage_events/ViewEventsComponent.vue';
import NewEventComponent from './manage_events/NewEventComponent.vue';
import { useEventStore } from '@/stores/eventStore';
import { useToast } from 'vue-toast-notification';

const showNewEventBox = ref(false);
const toggleNewEventBox = () => {
    showNewEventBox.value = !showNewEventBox.value;
    eventStore.clearTriggerNewEvent();
}

const showSingleEventBox = ref(false);
const eventStore = useEventStore();

const chooseEventId = (eventId: number) => {
    eventStore.chooseEvent(eventId);
    eventStore.fetchChosenEvent();
    showSingleEventBox.value = true;
};

const $toast = useToast();

const newEvent = () => {
    showNewEventBox.value = false;
    eventStore.clearTriggerNewEvent();
    eventStore.clearCoordinates();
    $toast.success('Ny hendelse opprettet!', {
        duration: 3000,
        position: 'top-right'
    });

}
const closeNewEventBox = () => {
    eventStore.clearTriggerNewEvent();
    eventStore.clearCoordinates();
    showNewEventBox.value = false;
};
watch(() => eventStore.chosenEvent, (newEvent) => {
    if (newEvent && newEvent.id !==  0 && newEvent.id !== undefined) {
        showSingleEventBox.value = true;
    } else {
        showSingleEventBox.value = false;
    }
});
</script>
<template>
<div class="container">
    <div class="header-box"> 
        <h1>Administrere Krisehendelser</h1>
        <div class="page-container">
            <p>Her kan du slette, endre og legge til nye krisehendelser.</p>
            <button class="dark-button" @click="toggleNewEventBox">+ Opprett ny hendelse</button>
        </div>  
    </div>   
    <div class="page-container">
        <ViewEventsComponent @event-selected="chooseEventId"/>
        <EditEventComponent v-if="showSingleEventBox" @hide-edit-box="showSingleEventBox = false"/>

        <div class="modal-overlay" v-if="showNewEventBox || eventStore.openNewEvent" @click.self="showNewEventBox = false">
            <NewEventComponent
            :lat="eventStore.lat"
            :lng="eventStore.lng"
            @close="closeNewEventBox"
            @hide-new-event-box="closeNewEventBox"
            @new-event-success="newEvent"
            />
        </div>
    </div>
</div>
</template>
<style scoped>
.header-box {
    margin-left: 2rem;
    margin-top: 4rem;
}

h1 {
    text-align: left;
    margin-bottom: 0.5rem;
}
p {
    margin-left: -1rem;
    text-align: left;
}
.page-container {
    display: flex;
    flex-direction: row;
    margin: 1rem;
}

.dark-button {
    height: 3rem;
    width: 12rem;
    background-color: var(--orange);
    margin-left: auto;
}

@media (max-width: 480px) {
    .page-container {
        flex-direction: column;
    }
    
    .modal-overlay {
        overflow-x: hidden;
        top: 0;
    }

    .dark-button {
        align-self: flex-start;
        margin-top: 1rem; 
        margin-left: -1rem; 
    }
}
</style>