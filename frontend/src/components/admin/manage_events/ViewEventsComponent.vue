<script lang="ts" setup>
import { ref } from 'vue';
import { useEventStore } from '@/stores/eventStore';

const events = ref([
    {id: 1, name: 'Event 1', description: 'Mega bombe', iconType: 'danger', startTime: new Date('2023-10-01'), latitude: 60.39299, longitude: 5.32415, radius: 1000, severity: 3},
    {id: 2, name: 'Event 2', description: 'Mindre bombe', iconType: 'danger', startTime: new Date('2024-10-01'), latitude: 60.39299, longitude: 5.32415, radius: 500, severity: 1},
    {id: 3, name: 'Event 3', description: 'Røde kors', iconType: 'medical', startTime: new Date('2025-05-01'), latitude: 60.39299, longitude: 5.32415, radius: 10, severity: 1},
])

// Store imports
const eventStore = useEventStore();

// Props
const selectedEventId = ref<number | null>(null)

// Choose an item in the supply
const chooseEvent = (eventId: any) => {
    selectedEventId.value = eventId;
    eventStore.setEvent(eventId);
}
</script>
<template>
<div class="page-container">
    <div class="header-box-container">
        <h1 class="medium-header">Krisehendelser</h1>
        <div class="grey-container">
            <div v-for="event in events" :key="event.id" class="item-card">
                <div :class="['article-card', { active: event.id === selectedEventId }]" @click="chooseEvent(event.id)">
                    <div class="info">
                        <h2>{{ event.name }}</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>
<style scoped>
    .page-container {
        margin: 1rem;
        display: flex;
        flex-direction: column;
    }

    .grey-container {
        height: auto;
    }

    .article-card {
        cursor: pointer;
    }

    .article-card.active, .article-card:hover {
        background-color: var(--light-blue); 
        color: white;
    }
</style>