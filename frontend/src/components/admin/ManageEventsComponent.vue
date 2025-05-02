<script lang="ts" setup>
import EditEventComponent from './manage_events/EditEventComponent.vue';
import ViewEventsComponent from './manage_events/ViewEventsComponent.vue';

</script>
<template>
<div class="header-container"> 
    <h1>Administrere Krisehendelser</h1>
    <p>Her kan du administrere krisehendelser. </p>
</div>   
<div class="page-container">
    <ViewEventsComponent />
    <EditEventComponent />
</div>
</template>
<style scoped>
.header-container {
    margin-left: 2rem;
}

.page-container {
    display: flex;
    flex-direction: row;
    margin: 1rem;
}
</style>