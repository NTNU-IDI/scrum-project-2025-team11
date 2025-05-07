<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue';
import { useEventStore } from '@/stores/eventStore';

// Store imports
const eventStore = useEventStore();

// Props
const events = ref<{ id: number; name: string; description: string; iconType: string; startTime: string; endTime: string; latitude: number; longitude: number; radius: number; severity: number }[]>([]);
const selectedEventId = ref<number>(0);
const emit = defineEmits(['event-selected']);

const loadEvents = async () => {
    await eventStore.fetchEvents();
};

onMounted(async () => {
    await loadEvents();
});

watch(() => eventStore.events, async (newEvents) => {
    events.value = newEvents.map(event => ({
        id: event.id,
        name: event.name,
        description: event.description,
        iconType: event.iconType,
        startTime: event.startTime,
        endTime: event.endTime,
        latitude: event.latitude,
        longitude: event.longitude,
        radius: event.radius,
        severity: event.severity
    }));
});

watch(() => eventStore.chosenEvent, async (newEvent) => {
    if (newEvent) {
        selectedEventId.value = newEvent.id;
    }
});

// Choose an item in the supply
const chooseEvent = (eventId: any) => {
    selectedEventId.value = eventId;
    emit('event-selected', eventId);
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