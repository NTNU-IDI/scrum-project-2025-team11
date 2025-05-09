<template>
  <div class="layout-map-page">
    <HeaderBase />
    <div class="map-page">
      <div v-if="showCrisisAlert" class="crisis-alert">
        <p><strong>Viktig melding:</strong> Du er i et kriseområde!</p>
      </div>
      <div class="corner-container" :class="{ 'crisis-mode': showCrisisAlert }">
        <IconsOverview ref="iconsOverviewRef"/>
        <EventsOverview />
          <button v-if="!isEditMode && showNearestShelterButton && !isNavigating" class="map-button-no-hover" @click="findNearestShelter">
            Finn 3 nærmeste tilflukstrom
          </button>  
          <button v-if="role === 'admin' && !isNavigating" id="editToggle" @click="toggleEditMode" :class="{ 'delete-button small-button-map': isEditMode, 'map-button-no-hover': !isEditMode }">
            {{ isEditMode ? 'Avslutt redigering' : 'Redigeringsmodus' }}
          </button>
        <SelectType 
          v-if="showSelectType"
          @add-point="handleAddPoint"
          @add-event="handleAddEvent"
          @close="closeSelectType" 
        />
        <PointView 
          v-if="showPointForm" 
          :selectedPoint="selectedPoint" 
          :mode="formMode"
          :isNavigating="isNavigating"
          @close="closePointForm" 
          @coordinates-updated="updateMarkerPosition"
          @navigate="handleNavigation"
          @next-shelter="handleNextShelter"
          @stop-navigation="clearRouting" 
          @close-point-view="closePointForm"
          :show-next-button="viewingNearest && nearestShelters.length > 1"        
        />
        <EventView v-if="showEventView" :event="selectedEvent" @close="closeEventView"/>
      </div>
      <div id="map" class="map"></div>
    </div>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/Footer.vue';
import HeaderBase from '@/components/HeaderBase.vue';
import EventsOverview from '../../components/map/EventsOverview.vue';
import IconsOverview from '../../components/map/IconsOverview.vue';
import PointView from '../../components/map/PointView.vue';
import SelectType from '../../components/map/SelectType.vue';
import EventView from '../../components/map/EventView.vue';
import L from 'leaflet';
import 'leaflet-routing-machine';
import 'leaflet/dist/leaflet.css';
import { usePointStore } from '@/stores/pointStore';
import { useUserStore } from "@/stores/userStore.ts";
import type { PointOfInterest } from "@/types/PointOfInterest";
import type { EventResponseDTO } from "@/types/Event";
import { isUserInCrisisArea } from '@/utils/geoService';
import { addMarkersToMap, addEventsToMap, clearEventLayers, createRoutingControl, clearRoutingControl, getUserPosition, setUserPositionMarker } from '@/services/MapService'; 
import { storeToRefs } from "pinia";
import { onUnmounted, onMounted, ref, watch } from 'vue';
import { useEventStore } from '@/stores/eventStore'; 
import { useToast } from 'vue-toast-notification';
import { useRouter } from 'vue-router'

const router = useRouter()
const $toast = useToast();
const eventStore = useEventStore(); 
const userStore = useUserStore()
const pointStore = usePointStore();
const {role} = storeToRefs(userStore)
const { pointsDisplaying } = storeToRefs(pointStore);
const { activeEvents } = storeToRefs(eventStore);
const showCrisisAlert = ref(false);
const showPointForm = ref(false);
const formMode = ref<'edit' | 'create' | 'view'>('create');
const currentShelterIndex = ref(0);
const nearestShelters = ref<PointOfInterest[]>([]);
const viewingNearest = ref(false);
const isNavigating = ref(false); 
const iconsOverviewRef = ref();
const isEditMode = ref(false);
const userLocationFetched = ref(false);
const showSelectType = ref(false);
const showNearestShelterButton = ref(true);
const showEventView = ref(false);
const selectedEvent = ref<EventResponseDTO>({ id: 0, name: '', description: '', iconType: '', startTime: '', endTime: '', latitude: 0, longitude: 0, radius: 0, severity: 0,});
const selectedPoint = ref<PointOfInterest>({ id: 0, name: '',description: '', iconType: '', latitude: 0, longitude: 0 });

let map: L.Map;
let temporaryMarker: L.Marker | null = null;
let markers: L.Marker[] = [];
let eventLayers: L.Circle[] = [];
let userMarker: L.Marker | null = null;
let userLat: number | null = null;
let userLon: number | null = null;

declare global {
  interface Window {
    routingControl: any;
  }
}
window.routingControl = null;

onMounted(async () => {
  map = L.map('map', {
    zoomControl: false
  }).setView([63.4305, 10.3951], 12);
  L.control.zoom({ position: 'topright' }).addTo(map);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

  pointStore.startPolling();
  eventStore.startPollingActiveEvents();

  // POIs
  addMarkersToMap(map, pointsDisplaying.value, markers, role, isEditMode, showPointView);
  watch(pointsDisplaying, () => {
    addMarkersToMap(map, pointsDisplaying.value, markers, role, isEditMode, showPointView);
  });

  // Events
  addEventsToMap(map, activeEvents.value, eventLayers, handleEventClick);
  watch(activeEvents, () => {
    addEventsToMap(map, activeEvents.value, eventLayers, handleEventClick);  
    checkIfInCrisisArea();
  });

  // Get user location and set marker
  const position = await getUserPosition(map);
  if (position) {
    userLat = position.lat;
    userLon = position.lon;
    checkIfInCrisisArea();
  }
});

onUnmounted(() => {
  pointStore.stopPolling();
  eventStore.stopPollingActiveEvents();
  isEditMode.value = false;
  $toast.clear();
});

function showPointView(mode: 'view' | 'edit' | 'create', point: PointOfInterest, showMarker: boolean, isViewingNearest: boolean) {
  clearRouting();
  removeTempMarker();
  viewingNearest.value = isViewingNearest;
  showEventView.value = false;
  selectedPoint.value = { ...point };
  formMode.value = mode;
  showSelectType.value = false;
  showPointForm.value = true;
  createTempMarker(point.latitude, point.longitude);
  if (showMarker) {
    map.setView([selectedPoint.value.latitude, selectedPoint.value.longitude], 15);
  }
}

const mapClickHandler = (e: L.LeafletMouseEvent) => {
  if (role.value === 'admin' && isEditMode.value) {
    const { lat, lng } = e.latlng;
    selectedPoint.value = {
      id: 0,
      name: '',
      description: '',
      iconType: '',
      latitude: lat,
      longitude: lng
    };
    if (!showPointForm.value) {
      removeTempMarker();
      createTempMarker(lat, lng);
      showSelectType.value = true;
    }
  }
};

function handleAddPoint() {
  showSelectType.value = false;
  showPointView('create', selectedPoint.value, true, false);
}

function handleAddEvent() {
  showSelectType.value = false;
  const roundedLat = parseFloat(selectedPoint.value.latitude.toFixed(7));
  const roundedLng = parseFloat(selectedPoint.value.longitude.toFixed(7));
  eventStore.setCoordinates(roundedLat, roundedLng);
  eventStore.triggerNewEvent();
  router.push('/admin');
}

watch(isEditMode, (newValue) => {
  checkIfInCrisisArea();
  if (role.value === 'admin') {
    if (newValue) {
      map.on('click', mapClickHandler);
    } else {
      if (mapClickHandler) {
        map.off('click', mapClickHandler);
      }
    }
  }
});

function closePointForm() {
  removeTempMarker();
  showPointForm.value = false;
  showNearestShelterButton.value = true;
}

function closeSelectType() {
  removeTempMarker();
  showSelectType.value = false;
}

function removeTempMarker() {
  if (temporaryMarker) {
    map.removeLayer(temporaryMarker);
    temporaryMarker = null;
  }
}

function createTempMarker(lat: number, lng: number) {
  temporaryMarker = L.marker([lat, lng]).addTo(map);
  if (formMode.value === 'create') {
    temporaryMarker.bindPopup("Nytt punkt her").openPopup();
  }
}

function updateMarkerPosition(coords: { latitude: number, longitude: number }) {
  removeTempMarker();
  createTempMarker(coords.latitude, coords.longitude);
  map.setView([coords.latitude, coords.longitude], map.getZoom());
  
  selectedPoint.value.latitude = coords.latitude;
  selectedPoint.value.longitude = coords.longitude;
}

function checkIfInCrisisArea() {
if (userLat !== null && userLon !== null) {
  if (!isEditMode.value) {
      showCrisisAlert.value = isUserInCrisisArea(userLat, userLon, activeEvents.value);
    }  
  } 
}

function handleEventClick(event: EventResponseDTO) {
  console
  clearRouting();
  showNearestShelterButton.value = true;
  selectedEvent.value = event;
  showPointForm.value = false;
  showEventView.value = true;
}

function closeEventView() {
  showEventView.value = false;
}

async function handleNavigation(coords: { latitude: number, longitude: number }) {
  clearRouting();
  const position = await getUserPosition(map);
  if (position) {
    userLat = position.lat;
    userLon = position.lon;
    window.routingControl = createRoutingControl(map, userLat, userLon, coords.latitude, coords.longitude);
    isNavigating.value = true;
  } else {
    handleNoPosition();
    isNavigating.value = false;
  } 
}

function clearRouting() {
  clearRoutingControl(map, window.routingControl);
  removeTempMarker();
  window.routingControl = null;
  isNavigating.value = false;
  showNearestShelterButton.value = true;
  if (userLat !== null && userLon !== null) {
    setUserPositionMarker(map, userLat, userLon);
  }
}

async function findNearestShelter() {
  isEditMode.value = false;

  const position = await getUserPosition(map);
  if (position) {
    userLat = position.lat;
    userLon = position.lon;
    await locateAndShowShelters(position.lat, position.lon);
    showNearestShelterButton.value = false;
  } else {
    handleNoPosition();
    showNearestShelterButton.value = true;
  }
}

function handleNoPosition() {
  $toast.clear();
  $toast.warning(
    "Vi har ikke tilgang til posisjonen din. Tillat posisjonstjenester i nettleseren for navigasjon og finne nærmeste tilfluktsrom.",
    { duration: 7000 }
  );
}

async function locateAndShowShelters(lat: number, lon: number) {
  if (!pointStore.selectedIcons.includes('shelter')) {
    iconsOverviewRef.value?.forceIncludeShelter();
    pointStore.updateSelectedIcons([...pointStore.selectedIcons, 'shelter']);
    await pointStore.fetchPointsByIconTypes(pointStore.selectedIcons);
  }

  nearestShelters.value = await pointStore.fetchNearestShelters(lat, lon);
  if (nearestShelters.value.length === 0) {
    $toast.warning('Ingen tilfluktsrom funnet i nærheten.', { duration: 5000 });
    return;
  }

  currentShelterIndex.value = 0;
  viewingNearest.value = true;
  
  const firstShelter = nearestShelters.value[0];
  map.setView([firstShelter.latitude, firstShelter.longitude], 15);
  showPointView('view', firstShelter, true, true);
}

function handleNextShelter() {
  if (nearestShelters.value.length <= 1) return;
  
  currentShelterIndex.value = 
    (currentShelterIndex.value + 1) % nearestShelters.value.length;
  showPointView('view', nearestShelters.value[currentShelterIndex.value], true, true);
}

function toggleEditMode() {
  $toast.clear();
  removeTempMarker();
  showPointForm.value = false;
  isEditMode.value = !isEditMode.value;

  if (isEditMode.value) {
    $toast.info('Redigeringsmodus aktivert. Klikk på et sted for å opprette et punkt, eller klikk på et eksisterende punkt for å redigere det.', { duration: 7000 });
    showCrisisAlert.value = false;
    clearEventLayers(eventLayers, map);
    showEventView.value = false;
    eventStore.stopPollingActiveEvents();
    
  } else {
    $toast.info('Redigeringsmodus deaktivert. Du er nå i visningsmodus.', { duration: 5000 });
    eventStore.startPollingActiveEvents();
    showSelectType.value = false;
    removeTempMarker();
    addEventsToMap(map, activeEvents.value, eventLayers, handleEventClick);
  }
}
</script>

<style>
.crisis-alert {
  position: absolute;
  width: 100%;
  background-color: var(--bad-red);
  color: var(--white);
  font-size: var(--font-size-small);
  font-weight: bold;
  text-align: center;
  z-index: 9999; 
}

.delete-button {
  border: 2px solid var(--bad-red);
  border-radius: 8px;
}

.dark-button:hover,
.delete-button:hover {
  transform: none; 
}

.delete-button:hover {
  background-color: var(--white); 
}
</style>