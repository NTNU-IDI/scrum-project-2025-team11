<template>
  <div class="layout-map-page">
    <Header />
    <div class="map-page">
      <div v-if="showCrisisAlert" class="crisis-alert">
        <p><strong>Viktig melding:</strong> Du er i et kriseområde!</p>
      </div>
      <div class="corner-container" :class="{ 'crisis-mode': showCrisisAlert }">
        <IconsOverview ref="iconsOverviewRef"/>
        <EventsOverview />
        <button v-if="!isEditMode && showNearestShelterButton" class="dark-button small-button" @click="findNearestShelter">
          Finn 3 nærmeste tilflukstrom
        </button>  
        <button id="editToggle" @click="toggleEditMode" :class="{ 'delete-button small-button': isEditMode, 'dark-button small-button': !isEditMode }">
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
      </div>

      <div id="map" class="map"></div>
    </div>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
import EventsOverview from '../../components/map/EventsOverview.vue';
import IconsOverview from '../../components/map/IconsOverview.vue';
import PointView from '../../components/map/PointView.vue';
import SelectType from '../../components/map/SelectType.vue';
import L from 'leaflet';
import 'leaflet-routing-machine';
import 'leaflet/dist/leaflet.css';
import userMarkerIcon from '@/assets/ikon/user-maker.svg';
import { usePointStore } from '@/stores/pointStore';
import { useUserStore } from "@/stores/userStore.ts";
import type { PointOfInterest } from "@/types/PointOfInterest";
import { calculateDistance, getEventColor } from '@/utils/geoService';
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
const selectedPoint = ref<PointOfInterest>({
  id: 0,
  name: '',
  description: '',
  iconType: '',
  latitude: 0,
  longitude: 0,
});
const userIcon = L.icon({
  iconUrl: userMarkerIcon,
  iconSize: [35, 35],   
  iconAnchor: [20, 40],
  popupAnchor: [0, -40],
});

let map: L.Map;
let temporaryMarker: L.Marker | null = null;
let markers: L.Marker[] = [];
let eventLayers: L.Circle[] = [];
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

  // POI and events
  addMarkersToMap();
  addEvents(map);
  watch(pointsDisplaying, () => {
    addMarkersToMap();
  });
  watch(activeEvents, () => {
    addEvents(map);
    updateCrisisAlertVisibility();
  });

  // Get user location and set marker
  getUserPosition((lat, lon) => {
    map.setView([lat, lon], 13);
    checkIfInCrisisArea(lat, lon);
  });
});

onUnmounted(() => {
  pointStore.stopPolling();
  eventStore.stopPollingActiveEvents();
  isEditMode.value = false;
  $toast.clear();
});

function showPointView(mode: 'view' | 'edit' | 'create', point: PointOfInterest, showMarker: boolean) {
  clearRouting();
  removeTempMarker();
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
  console.log(showPointForm.value);
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
      showSelectType.value = true;
    }
  }
};

function handleAddPoint() {
  showSelectType.value = false;
  showPointView('create', selectedPoint.value, true);
}

function handleAddEvent() {
  showSelectType.value = false;
  eventStore.setCoordinates(selectedPoint.value.latitude, selectedPoint.value.longitude);
  router.push('/admin');
  // TODO: Navigate to admin view and click on new event button and prefill lat and lng
}

watch(isEditMode, (newValue) => {
  updateCrisisAlertVisibility();
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

function addMarkersToMap() {
  markers.forEach(marker => map.removeLayer(marker));
  markers = [];
  pointsDisplaying.value.forEach(point => {
    const customIcon = L.divIcon({
      html: `<div class="map-icon ${point.iconType}" style="margin: 0;"></div>`,
      className: '',
      iconSize: [20, 20],
      iconAnchor: [10, 10],
    });

    const marker = L.marker([point.latitude, point.longitude], { icon: customIcon }).addTo(map);
    markers.push(marker);

    // Marker click behavior for admin and non-admin users
    marker.on('click', () => {
      viewingNearest.value = false;

      if (role.value === 'admin' && isEditMode.value) {
        showPointView('edit', point, true);
      } else {
        showPointView('view', point, true);
      }
    });
  });
}

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

function getUserPosition(callback: (lat: number, lon: number) => void, force: boolean = false) {
  if (!navigator.geolocation || (userLocationFetched.value && !force)) return;

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      userLat = pos.coords.latitude;
      userLon = pos.coords.longitude;
      userLocationFetched.value = true;
      callback(userLat, userLon);

      L.marker([userLat, userLon], { icon: userIcon })
        .addTo(map)
        .bindPopup("Din posisjon")
        .openPopup();

      map.setView([userLat, userLon], 13);
    },
    (err) => console.error("Error getting location: ", err)
  );
}

function isInCrisisArea(lat: number, lon: number): boolean {
  return activeEvents.value.some(event => 
    calculateDistance(lat, lon, event.latitude, event.longitude) <= event.radius
  );
}

function updateCrisisAlertVisibility() {
  if (userLat !== null && userLon !== null) {
    showCrisisAlert.value = isInCrisisArea(userLat, userLon) && !isEditMode.value;
  }
}


function checkIfInCrisisArea(userLatitude: number, userLongitude: number) {
  showCrisisAlert.value = false;
  activeEvents.value.forEach(event => {
    const distance = calculateDistance(userLatitude, userLongitude, event.latitude, event.longitude);
    
    if (distance <= event.radius) {
      showCrisisAlert.value = true;
    }
  });
}

function addEvents(map: L.Map) {
  clearEventLayers();
  activeEvents.value.forEach(event => {
    const color = getEventColor(event.severity);
    const circle = L.circle([event.latitude, event.longitude], {
      color,
      fillColor: color,
      weight: 1,
      radius: event.radius,
      fillOpacity: 0.3
    }).addTo(map);
    eventLayers.push(circle);
  });
}

function clearEventLayers() {
  eventLayers.forEach(layer => map.removeLayer(layer));
  eventLayers = [];
}

function handleNavigation(coords: { latitude: number, longitude: number }) {
  getUserPosition((userLat, userLon) => {
    clearRouting();

    window.routingControl = L.Routing.control({
      waypoints: [L.latLng(userLat, userLon), L.latLng(coords.latitude, coords.longitude)],
      routeWhileDragging: false,
      createMarker: () => L.marker([userLat, userLon], { icon: userIcon }),
      lineOptions: {
        styles: [
          {
            color: 'var(--navigation)',
            weight: 6  
          }
        ]
      } as L.Routing.LineOptions
    } as L.Routing.RoutingControlOptions).addTo(map);

    isNavigating.value = true;
    console.log('hei');
  });
}

function clearRouting() {
  if (window.routingControl) {
    map.removeControl(window.routingControl);
    window.routingControl = null;
  }
  isNavigating.value = false;
}

async function findNearestShelter() {
  isEditMode.value = false;
  showNearestShelterButton.value = false;
  getUserPosition(() => {}, true);
  if (!pointStore.selectedIcons.includes('shelter')) {
    iconsOverviewRef.value?.forceIncludeShelter();
    pointStore.updateSelectedIcons([...pointStore.selectedIcons, 'shelter']);
    await pointStore.fetchPointsByIconTypes(pointStore.selectedIcons);
  }
  const center = map.getCenter();
  const lat = center.lat;
  const lon = center.lng;
  nearestShelters.value = await pointStore.fetchNearestShelters(lat, lon);

  if (nearestShelters.value.length === 0) {
    $toast.warning('Ingen tilfluktsrom funnet i nærheten.', { duration: 5000 });
    return;
  }
  currentShelterIndex.value = 0;
  viewingNearest.value = true;
  showPointView('view', nearestShelters.value[currentShelterIndex.value], true);
}

function handleNextShelter() {
  if (nearestShelters.value.length <= 1) return;
  
  currentShelterIndex.value = 
    (currentShelterIndex.value + 1) % nearestShelters.value.length;
  showPointView('view', nearestShelters.value[currentShelterIndex.value], true);
}

function toggleEditMode() {
  $toast.clear();
  showPointForm.value = false;
  isEditMode.value = !isEditMode.value;

  if (isEditMode.value) {
    $toast.info('Redigeringsmodus aktivert. Klikk på et sted for å opprette et punkt, eller klikk på et eksisterende punkt for å redigere det.', { duration: 7000 });
    clearEventLayers();
    eventStore.stopPollingActiveEvents();
    
  } else {
    $toast.info('Redigeringsmodus deaktivert. Du er nå i visningsmodus.', { duration: 5000 });
    eventStore.startPollingActiveEvents();
    removeTempMarker();
    addEvents(map);
  }
}
</script>

<style>
.corner-container {
  position: absolute;
  top: 10px;
  left: 10px;
  bottom: 10px;
  z-index: 2;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-height: calc(100% - 20px); 
  overflow: hidden; 
}

.corner-container.crisis-mode {
  top: 45px;
}

.layout-map-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.map-page {
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.map {
  flex: 1;
  width: 100%;
  z-index: 0;
}

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

.crisis-alert p {
  padding: 10px 10px;
  margin: 0;
}

.leaflet-touch .leaflet-control-attribution, .leaflet-touch .leaflet-control-layers, .leaflet-touch .leaflet-bar {
    display: none;
}

.leaflet-marker-icon.leaflet-interactive, .leaflet-image-layer.leaflet-interactive, .leaflet-pane > svg path.leaflet-interactive, svg.leaflet-image-layer.leaflet-interactive path {
    display: block;
    visibility: visible;
    pointer-events: auto;
}

.leaflet-interactive[stroke][fill-opacity] {
  pointer-events: none;
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

@media (max-width: 768px) {
  .crisis-alert {
    font-size: var(--font-size-xsmall);
  }
}
</style>