<template>
  <div class="point-card">
    <span class="close-icon" @click="$emit('close')">×</span>

    <div class="point-card-content">

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
        <button class="delete-button" @click="deletePoint">Slett</button>
      </div>
    </div>
  </div>
</template>


<script lang="ts" setup>
import { defineProps, type PropType, ref, watch, computed } from 'vue';
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

const formTitle = computed(() => props.selectedPoint ? 'Endre punkt' : 'Nytt punkt');
const buttonText = computed(() => props.selectedPoint ? 'Lagre' : 'Opprett');
const isEdit = computed(() => !!props.selectedPoint);

const savePoint = async () => {
  try {
    await pointStore.updatePointById(pointData.value);

  } catch (error) {
    alert("Kunne ikke oppdatere punktet.");
  }
};

const deletePoint = async () => {
  // TODO: Confirmation
  // TODO: Pointstore -> backend
};

</script>

<style scoped>
.edit-buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.close-icon {
  position: absolute;
  top: 5px;
  right: 10px;
  font-size: 24px;
  color: black;
  cursor: pointer;
  user-select: none;
}

.close-icon:hover {
  color: var(--bad-red, red);
}
</style>