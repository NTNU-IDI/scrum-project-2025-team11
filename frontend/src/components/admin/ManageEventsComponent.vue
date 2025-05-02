<script lang="ts" setup>
import { ref } from 'vue';
import EditEventComponent from './manage_events/EditEventComponent.vue';
import ViewEventsComponent from './manage_events/ViewEventsComponent.vue';
import NewEventComponent from './manage_events/NewEventComponent.vue';

const showNewEventBox = ref(false);
const toggleNewEventBox = () => {
    showNewEventBox.value = !showNewEventBox.value;
}
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
    <ViewEventsComponent />
    <EditEventComponent />
    <div v-if="showNewEventBox">
        <NewEventComponent @hide-new-event-box="toggleNewEventBox" />
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