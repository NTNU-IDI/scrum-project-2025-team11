<template>
  <div class="point-card">
    <span class="close-icon" @click="$emit('close')">×</span>

    <div class="point-card-content">

      <h1 class="title-map">{{ formTitle }}</h1>

      <input v-model="pointData.name" type="text" placeholder="Navn" />

      <select v-model="pointData.iconType">
        <option disabled value="">Velg type punkt</option>
        <option value="shelter">Tilfluktsrom</option>
        <option value="assembly_point">Møteplass</option>
        <option value="medical">Medisinsk hjelp</option>
      </select>

      <input v-model="pointData.description" placeholder="Beskrivelse" />

      <div class="coordinates-input">
        <input v-model="pointData.latitude" type="number" placeholder="Breddegrad" />
        <input v-model="pointData.longitude" type="number" placeholder="Lengdegrad" />
      </div>

      <p v-if="validationError" class="error-message">{{ validationError }}</p>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

      <!-- Create mode buttons -->
      <div v-if="!isEdit" class="point buttons create-buttons">
        <button class="button" @click="createPoint" :disabled="hasValidationError">Lag nytt punkt</button>
      </div>
      <!-- Edit modebuttons -->
      <div v-else class="point-buttons edit-buttons">
        <button v-if="isEdit" class="button" @click="savePoint" :disabled="hasValidationError">Lagre punkt</button>
        <button v-if="isEdit" class="delete-button" @click="deletePoint">Slett</button>
      </div>
    </div>
  </div>
</template>


<script lang="ts" setup>
import { type PointOfInterest, usePointStore } from '@/stores/pointStore';
import { computed, defineProps, type PropType, ref, watch } from 'vue';
import {
  validatePointName,
  validatePointDescription,
  validateIconType,
  validateLatitude,
  validateLongitude
} from '@/utils/validationService';

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
const validationError = ref('');
const errorMessage = ref('');

const hasValidationError = computed(() => {
  if (!validatePointName(pointData.value.name)) {
    validationError.value = "Ugyldig navn. Bruk kun bokstaver, tall og noen skilletegn.";
    return true;
  }
  if (!validateIconType(pointData.value.iconType)) {
    validationError.value = "Velg en gyldig type for punktet.";
    return true;
  }
  if (!validatePointDescription(pointData.value.description)) {
    validationError.value = "Beskrivelsen må være på minst 5 tegn.";
    return true;
  }
  if (!validateLatitude(pointData.value.latitude)) {
    validationError.value = "Breddegrad må være et tall mellom -90 og 90.";
    return true;
  }
  if (!validateLongitude(pointData.value.longitude)) {
    validationError.value = "Lengdegrad må være et tall mellom -180 og 180.";
    return true;
  }

  validationError.value = '';
  return false;
});

const createPoint = async () => {
  errorMessage.value = '';
  try {
    await pointStore.createPoint(pointData.value);

  } catch (error) {
    errorMessage.value = "Kunne ikke lage det nye punktet.";
  }
};

const savePoint = async () => {
  errorMessage.value = '';
  try {
    await pointStore.updatePointById(pointData.value);

  } catch (error) {
    errorMessage.value = "Kunne ikke oppdatere punktet.";
  }
};

const deletePoint = async () => {
  const confirmDelete = confirm("Er du sikker på at du vil slette dette punktet?");
  
  if (confirmDelete) {
    try {
      await pointStore.deletePointById(pointData.value.id);
    } catch (error) {
      errorMessage.value = "Kunne ikke slette punktet.";
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
  color: var(--bad-red);
}
</style>