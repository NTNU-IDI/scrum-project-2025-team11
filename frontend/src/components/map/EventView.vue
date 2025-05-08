<template>
  <div class="point-card">
    <span class="close-icon" @click="$emit('close')">×</span>

      <div class="point-card-content">
        <h1 class="title-map">{{ eventData.name }}</h1>

        <div class="point-detail-container">
          <div class="point-detail">
            <strong>Beskrivelse:</strong>
            <div>{{ eventData.description }}</div>
          </div>

          <div class="point-detail">
            <strong>Alvorlighetsgrad:</strong>
            <div>{{ eventData.severity }}</div>
          </div>

          <div class="point-detail">
            <strong>Starttid:</strong>
            <div>{{ eventData.startTime }}</div>
          </div>

          <div class="point-detail" v-if="eventData.endTime">
            <strong>Sluttid:</strong>
            <div>{{ eventData.endTime }}</div>
          </div>

          <div class="point-detail">
            <strong>Koordinater:</strong>
            <div>{{ eventData.latitude }}, {{ eventData.longitude }}</div>
          </div>

          <div class="point-detail" v-if="address && !addressError">
            <strong>Adresse:</strong>
            <div>{{ address }}</div>
          </div>

          <div class="point-detail">
            <strong>Radius (meter):</strong>
            <div>{{ eventData.radius }}</div>
          </div>

          <p v-if="addressError" class="error-message">{{ addressError }}</p>
        </div>

      </div>
    </div>
</template>


<script lang="ts" setup>
import type { EventResponseDTO } from "@/types/Event";
import { computed, defineProps, type PropType, ref, watch, defineEmits } from 'vue';

const address = ref('');
const addressError = ref('');
const emit = defineEmits(['close', 'coordinates-updated']);
const eventData = computed(() => props.event);

const props = defineProps({
  event: {
    type: Object as PropType<EventResponseDTO>,
    required: true,
  }
});

watch(
  () => [eventData.value.latitude, eventData.value.longitude],
  ([lat, lon]) => {
    if (lat && lon) {
      resolveCoords(lat, lon);
    }
  },
  { immediate: true }
);

async function resolveCoords(lat: number, lon: number) {
  try {
    const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`);
    const data = await response.json();
    if (data?.display_name) {
      address.value = data.display_name;
    } else {
      address.value = '';
      addressError.value = 'Fant ikke adresse for koordinatene.';
    }
  } catch (err) {
    addressError.value = 'Feil ved oppslag av adresse.';
  }
}
</script>

<style scoped>
@media (max-width: 768px) {
  .point-detail {
    margin-bottom: 10px;
    font-size: var(--font-size-xsmall);
  }
}
</style>