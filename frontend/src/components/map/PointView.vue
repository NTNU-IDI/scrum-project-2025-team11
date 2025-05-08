<template>
  <div class="point-card">
    <span class="close-icon" @click="$emit('close')">×</span>

      <!-- View mode (for registered and non-registered users) -->
      <div v-if="isViewMode" class="point-card-content">
        <h1 class="title-map">{{ pointData.name }}</h1>

        <div class="point-detail-container">
          <div class="point-detail">
            <strong>Type punkt:</strong>
            <div>
              {{
                pointData.iconType === 'shelter'
                  ? 'Tilfluktsrom'
                  : pointData.iconType === 'assembly_point'
                  ? 'Møteplass'
                  : 'Medisinsk hjelp'
              }}
            </div>
          </div>

          <div class="point-detail">
            <strong>Beskrivelse:</strong>
            <div>{{ pointData.description }}</div>
          </div>

          <div class="point-detail">
            <strong>Koordinater:</strong>
            <div>{{ pointData.latitude }}, {{ pointData.longitude }}</div>
          </div>

          <div class="point-detail" v-if="address && !addressError">
            <strong>Adresse:</strong>
            <div>{{ address }}</div>
          </div>

          <p v-if="addressError" class="error-message">{{ addressError }}</p>

          <!-- Navigation button -->
           <div class="point-buttons">
            <button v-if="!props.isNavigating"class="good-button small-button" @click="navigateToPoint">Naviger til dette punktet</button>
            <button v-if="props.isNavigating" class="delete-button small-button" @click="stopNavigation">Stopp navigasjon</button>
            <button v-if="showNextButton && !props.isNavigating" class="dark-button small-button" @click="nextShelter">Neste tilfluktsrom</button>
           </div>
        </div>
      </div>


      <!-- Edit/Create mode (for admins) -->
      <div v-else class="point-card-content">
        <h1 class="title-map">{{ formTitle }}</h1>

        <div class="point-detail-container">
          <!-- Name -->
          <label class="point-label">Navn*</label>
          <input v-model="pointData.name" type="text" class="point-input" />

          <!-- Icon type -->
          <label class="point-label">Type punkt*</label>
          <select class="point-select" v-model="pointData.iconType">
            <option disabled value="">Velg type punkt</option>
            <option value="shelter">Tilfluktsrom</option>
            <option value="assembly_point">Møteplass</option>
            <option value="medical">Medisinsk hjelp</option>
          </select>

          <!-- Description -->
          <label class="point-label">Beskrivelse*</label>
          <input class="point-input" v-model="pointData.description" placeholder="F.eks. Plass til 5." />

          <!-- Choose coordinates or address -->
          <label class="point-label">Inndata for lokasjon*</label>
          <select class="point-select" v-model="inputMethod">
            <option disabled value="">Velg inndata for lokasjon</option>
            <option value="coordinates">Koordinater</option>
            <option value="address">Adresse</option>
          </select>

          <!-- Coordinates -->
          <div v-if="inputMethod === 'coordinates'" class="coordinates-input">
            <label class="point-label">Breddegrad</label>
            <input class="point-input" v-model="pointData.latitude" type="number" placeholder="F.eks. 63,41" />
            <label class="point-label">Lengdegrad</label>
            <input class="point-input" v-model="pointData.longitude" type="number" placeholder="F.eks. 10,40" />
          </div>

          <!-- Address -->
          <div v-if="inputMethod === 'address'">
            <label class="point-label">Adresse</label>
            <input class="point-input" v-model="address" type="text" @blur="resolveAddress" />
            <p v-if="addressError" class="error-message">{{ addressError }}</p>
          </div>

          <!-- Validation error messages -->
          <p v-if="validationError" class="error-message">{{ validationError }}</p>

          <!-- Create mode buttons -->
          <div v-if="!isEdit" class="point buttons create-buttons">
            <button class="good-button small-button" @click="createPoint" :disabled="hasValidationError">Lag nytt punkt</button>
          </div>
          <!-- Edit modebuttons -->
          <div v-else class="point-buttons edit-buttons">
            <button v-if="isEdit" class="good-button small-button" @click="savePoint" :disabled="hasValidationError">Lagre punkt</button>
            <button v-if="isEdit" class="delete-button small-button" @click="deletePoint">Slett</button>
          </div>
        </div>
      </div>
  </div>
</template>


<script lang="ts" setup>
import type { PointOfInterest } from "@/types/PointOfInterest";
import { usePointStore } from '@/stores/pointStore';
import { computed, defineProps, type PropType, ref, watch, defineEmits } from 'vue';
import {
  validatePointName,
  validatePointDescription,
  validateIconType,
  validateLatitude,
  validateLongitude
} from '@/utils/validationService';

const pointStore = usePointStore();
const isEdit = computed(() => props.mode === 'edit');
const isViewMode = computed(() => props.mode === 'view');
const formTitle = computed(() => isEdit.value ? 'Endre punkt' : 'Nytt punkt');
const validationError = ref('');
const inputMethod = ref('coordinates');
const address = ref('');
const addressError = ref('');
const emit = defineEmits(['close', 'coordinates-updated', 'navigate', 'next-shelter', 'stop-navigation', 'close-point-view']);

const props = defineProps({
  selectedPoint: {
    type: Object as PropType<PointOfInterest>,
    required: true,
  },
  mode: {
    type: String as PropType<'edit' | 'create' | 'view'>,
    default: 'create'
  },
  showNextButton: {
    type: Boolean,
    default: false
  },
  isNavigating: { 
    type: Boolean,
    default: false
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
watch(() => props.selectedPoint, async (newPoint) => {
  pointData.value = { ...newPoint };

  // Regenerate adress from coordinates
  if (newPoint.latitude && newPoint.longitude) {
    await resolveCoords(newPoint.latitude, newPoint.longitude);
  }
}, { immediate: true });

// Watch for coordinate changes and emit event
watch(() => [pointData.value.latitude, pointData.value.longitude], ([newLat, newLon]) => {
  if (newLat && newLon && !isNaN(newLat) && !isNaN(newLon)) {
    emit('coordinates-updated', { latitude: newLat, longitude: newLon });
  }
}, { deep: true });

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

async function resolveAddress() {
  if (!address.value.trim()) return;

  try {
    const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address.value)}`);
    const data = await response.json();

    if (data.length > 0) {
      const newLat = parseFloat(data[0].lat);
      const newLon = parseFloat(data[0].lon);

      pointData.value.latitude = newLat;
      pointData.value.longitude = newLon;
      addressError.value = '';
      
      // Emit event with new coordinates
      emit('coordinates-updated', { 
        latitude: newLat,
        longitude: newLon
      });
    } else {
      addressError.value = 'Fant ingen koordinater for adressen.';
    }
  } catch (err) {
    addressError.value = 'En feil skjedde ved oppslag av adresse.';
  }
}

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

function navigateToPoint() {
  emit('navigate', {
    latitude: pointData.value.latitude,
    longitude: pointData.value.longitude
  });
}

function nextShelter() {
  emit('next-shelter');
}

function stopNavigation() {
  emit('stop-navigation');
}

const createPoint = async () => {
  try {
    await pointStore.createPoint(pointData.value);
    emit('close-point-view');

  } catch (error) {
    throw error;
  }
};

const savePoint = async () => {
  try {
    await pointStore.updatePointById(pointData.value);
    emit('close-point-view');

  } catch (error) {
    throw error;
  }
};

const deletePoint = async () => {
  const confirmDelete = confirm("Er du sikker på at du vil slette dette punktet?");
  
  if (confirmDelete) {
    try {
      await pointStore.deletePointById(pointData.value.id);
      emit('close-point-view');

    } catch (error) {
      throw error;
    }
  }
};

</script>

<style scoped>
.point-label {
  display: block;
  font-size: var(--font-size-xsmall);
}

.point-buttons {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.point-input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 7px;
  border: none;
  border-radius: 5px;
  background-color: var(--grey);
  font-size: 1rem;
  box-sizing: border-box;
  font-size: var(--font-size-small);
}

.point-select {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 7px;
  border: none;
  border-radius: 5px;
  background-color: var(--grey);
  font-size: 1rem;
  box-sizing: border-box;
  font-size: var(--font-size-small);
}

@media (max-width: 768px) {
  .point-detail {
    margin-bottom: 10px;
    font-size: var(--font-size-xsmall);
  }
}
</style>