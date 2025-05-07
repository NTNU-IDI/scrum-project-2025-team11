<script lang="ts" setup>
import { ref, watch } from 'vue';
import EditEventComponent from './manage_events/EditEventComponent.vue';
import ViewEventsComponent from './manage_events/ViewEventsComponent.vue';
import NewEventComponent from './manage_events/NewEventComponent.vue';
import { useEventStore } from '@/stores/eventStore';

const showNewEventBox = ref(false);
const toggleNewEventBox = () => {
    showNewEventBox.value = !showNewEventBox.value;
}

const showSingleEventBox = ref(false);
const eventStore = useEventStore();
const chooseEventId = (eventId: number) => {
    eventStore.chooseEvent(eventId);
    eventStore.fetchChosenEvent();
    showSingleEventBox.value = true;
};

watch(() => eventStore.chosenEvent, (newEvent) => {
    if (newEvent && newEvent.id !==  undefined) {
        showSingleEventBox.value = true;
    } else {
        showSingleEventBox.value = false;
    }
});
</script>
<template>
<div class="header-container"> 
    <h1>Administrere Krisehendelser</h1>
    <div class="page-container">
        <p>Her kan du administrere krisehendelser.</p>
        <button class="dark-button" @click="toggleNewEventBox">+ Opprett ny hendelse</button>
    </div>  
</div>   
<div class="page-container">
    <ViewEventsComponent @event-selected="chooseEventId"/>
    <EditEventComponent v-if="showSingleEventBox" @hide-edit-box="showSingleEventBox = false"/>

    <div class="modal-overlay" v-if="showNewEventBox" @click.self="showNewEventBox = false">
        <NewEventComponent
        @close="showNewEventBox = false"
        @hide-new-event-box="showNewEventBox = false"
        />
    </div>
</div>
</template>
<style scoped>
.header-container {
    margin-left: 2rem;
    margin-top: 4rem;
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
</style>