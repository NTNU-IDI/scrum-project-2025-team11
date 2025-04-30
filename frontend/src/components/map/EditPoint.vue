<template>
  <div class="register-login-container">
    <h1 class="title-map">Endre punkt</h1>

    <input v-model="pointData.name" placeholder="Navn" />

    <select v-model="pointData.iconType">
      <option disabled value="">Velg type punkt</option>
      <option value="shelter">Tilfluktsrom</option>
      <option value="assembly_point">Møteplass</option>
      <option value="medical">Medisinsk hjelp</option>
    </select>

    <input v-model="pointData.description" placeholder="Beskrivelse" />

    <div class="coordinates-input">
      <input v-model="pointData.latitude" placeholder="Breddegrad" />
      <input v-model="pointData.longitude" placeholder="Lengdegrad" />
    </div>

    <div class="edit-buttons">
      <button class="button" @click="savePoint">Lagre</button>
      <button class="grey-button" @click="$emit('close')">Cancel</button>
    </div>

  </div>
</template>


<script lang="ts" setup>
import { defineProps, type PropType, ref, watch } from 'vue';
import { type PointOfInterest, usePointStore } from '@/stores/pointStore';

const pointStore = usePointStore();

const props = defineProps({
  selectedPoint: {
    type: Object as PropType<PointOfInterest | null>,
    required: true,
  }
});

// Copy of data to modify
const pointData = ref<PointOfInterest>({
  id: 0,
  name: '',
  description: '',
  iconType: '',
  latitude: 0,
  longitude: 0,
});

// Update data whenever prop changes
watch(() => props.selectedPoint, (newPoint) => {
  if (newPoint) {
    pointData.value = { ...newPoint };
  }
}, { immediate: true });

const savePoint = () => {
  // TODO: Call to pointStore -> call to backend
}
</script>

<style scoped>
.edit-buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
</style>