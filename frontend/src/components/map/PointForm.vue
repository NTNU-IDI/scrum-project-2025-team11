<template>
  <div class="point-card">
    <span class="close-icon" @click="$emit('close')">×</span>

    <div class="point-card-content">

      <h1 class="title-map">{{ formTitle }}</h1>

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

      <!-- Create mode buttons -->
      <div v-if="!isEdit" class="point buttons create-buttons">
        <button class="button" @click="createPoint">Lag nytt punkt</button>
      </div>
      <!-- Edit modebuttons -->
      <div v-else class="point-buttons edit-buttons">
        <button v-if="isEdit" class="button" @click="savePoint">Lagre punkt</button>
        <button v-if="isEdit" class="delete-button" @click="deletePoint">Slett</button>
      </div>
    </div>
  </div>
</template>


<script lang="ts" setup>
import { type PointOfInterest, usePointStore } from '@/stores/pointStore';
import { computed, defineProps, type PropType, ref, watch } from 'vue';

const pointStore = usePointStore();

const props = defineProps({
  selectedPoint: {
    type: Object as PropType<PointOfInterest>,
    required: true,
  },
  mode: {
    type: String as PropType<'edit' | 'create'>,
    default: 'create'
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
  pointData.value = { ...newPoint };
}, { immediate: true });

const isEdit = computed(() => props.mode === 'edit');
const formTitle = computed(() => isEdit.value ? 'Endre punkt' : 'Nytt punkt');

const createPoint = async () => {
  try {
    await pointStore.createPoint(pointData.value);

  } catch (error) {
    alert("Kunne ikke lage det nye punktet.");
  }
};

const savePoint = async () => {
  try {
    await pointStore.updatePointById(pointData.value);

  } catch (error) {
    alert("Kunne ikke oppdatere punktet.");
  }
};

const deletePoint = async () => {
  const confirmDelete = confirm("Er du sikker på at du vil slette dette punktet?");
  
  if (confirmDelete) {
    try {
      await pointStore.deletePointById(pointData.value.id);
    } catch (error) {
      alert("Kunne ikke slette punktet.");
    }
  }
};

</script>

<style scoped>
.point-buttons {
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