<template>
  <div class="layout-map-page">
    <Header />
    <div class="map-page">
      <div class="corner-container">
        <IconsOverview ref="iconsOverviewRef"/>
        <EventsOverview />
        <button class="dark-button small-button" @click="findNearestShelter">
          Finn 3 nærmeste tilflukstrom
        </button>        
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
          :show-next-button="viewingNearest && nearestShelters.length > 1"        
        />
      </div>

      <div id="map" class="map"></div>

      <div v-if="showCrisisAlert" class="crisis-alert">
        <p><strong>Viktig melding:</strong> Du er i et kriseområde!</p>
      </div>
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
import L from 'leaflet';
import 'leaflet-routing-machine';
import 'leaflet/dist/leaflet.css';
import userMarkerIcon from '@/assets/ikon/user-maker.svg';
import { usePointStore } from '@/stores/pointStore';
import { useUserStore } from "@/stores/userStore.ts";
import type { PointOfInterest } from "@/types/PointOfInterest";
import { calculateDistance, getEventColor } from '@/utils/geoService';
import { storeToRefs } from "pinia";
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
import { useEventStore } from '@/stores/eventStore'; 

const eventStore = useEventStore(); 
const userStore = useUserStore()
const pointStore = usePointStore();
const {role} = storeToRefs(userStore)
const { pointsDisplaying } = storeToRefs(pointStore);
const showCrisisAlert = ref(false);
const showPointForm = ref(false);
const formMode = ref<'edit' | 'create' | 'view'>('create');
const currentShelterIndex = ref(0);
const nearestShelters = ref<PointOfInterest[]>([]);
const viewingNearest = ref(false);
const isNavigating = ref(false); 
const iconsOverviewRef = ref();
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

declare global {
  interface Window {
    routingControl: any;
  }
}
window.routingControl = null;

onMounted(async () => {
  // Init map
  map = L.map('map', {
    zoomControl: false
  }).setView([63.4305, 10.3951], 12);
  L.control.zoom({ position: 'topright' }).addTo(map);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

  await pointStore.initializePolling();

  // POI and events
  addMarkersToMap();
  addEvents(map);

  watch(pointsDisplaying, () => {
    updateMarkers();
  });

  // Get user location and set marker
  getUserPosition((lat, lon) => {
    map.setView([lat, lon], 13);
    checkIfInCrisisArea(lat, lon);
  });

  // ADMIN: New point on click
  if (role.value === 'admin') {
    map.on('click', (e: L.LeafletMouseEvent) => {
    const { lat, lng } = e.latlng;
    removeTempMarker();
    createTempMarker(lat, lng);
    selectedPoint.value = {
        id: 0,
        name: '',
        description: '',
        iconType: '',
        latitude: lat,
        longitude: lng
      };
      formMode.value = 'create';
      showPointForm.value = true;
    });
  }
});

// Stop polling when component is unmounted
onBeforeUnmount(() => {
  pointStore.stopPolling();
});

function addMarkersToMap() {
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
      clearRouting();
      removeTempMarker();
      selectedPoint.value = { ...point };
      viewingNearest.value = false;

      if (role.value === 'admin') {
        removeTempMarker();
        formMode.value = 'edit';
        showPointForm.value = true;
        viewingNearest.value = false;
        createTempMarker(selectedPoint.value.latitude, selectedPoint.value.longitude);
      } else {
        formMode.value = 'view';
        showPointForm.value = true; 
        createTempMarker(selectedPoint.value.latitude, selectedPoint.value.longitude);       
      }
    });
  });
}

function updateMarkers() {
  // Remove all existing markers
  markers.forEach(marker => map.removeLayer(marker));
  markers = [];

  // Re-add markers for the updated points
  addMarkersToMap();
}

function closePointForm() {
  removeTempMarker();
  showPointForm.value = false;
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
  // Update marker position
  removeTempMarker();
  createTempMarker(coords.latitude, coords.longitude);
  map.setView([coords.latitude, coords.longitude], map.getZoom());
  
  // Update selectedPoint with new coordinates
  selectedPoint.value.latitude = coords.latitude;
  selectedPoint.value.longitude = coords.longitude;
}

function getUserPosition(callback: (lat: number, lon: number) => void) {
  // Return if browser does not support geolocation
  if (!navigator.geolocation) return;

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      callback(pos.coords.latitude, pos.coords.longitude);
      const userLat = pos.coords.latitude;
      const userLon = pos.coords.longitude;

      L.marker([userLat, userLon], { icon: userIcon })
        .addTo(map)
        .bindPopup("Din posisjon")
        .openPopup();

      map.setView([userLat, userLon], 13);
      checkIfInCrisisArea(userLat, userLon);
    },
    (err) => console.error("Error getting location: ", err)
  );
}

function checkIfInCrisisArea(userLatitude: number, userLongitude: number) {
  eventStore.events.forEach(event => {
    const distance = calculateDistance(userLatitude, userLongitude, event.latitude, event.longitude);
    
    if (distance <= event.radius) {
      showCrisisAlert.value = true;
    }
  });
}

function addEvents(map: L.Map) {
  if (eventStore.activeEvents.length === 0) {
    return;
  }
  eventStore.activeEvents.forEach(event => {
    const color = getEventColor(event.severity);
    L.circle([event.latitude, event.longitude], {
      color,
      fillColor: color,
      weight: 1,
      radius: event.radius,
      fillOpacity: 0.3
    }).addTo(map);
  });
}

function handleNavigation(coords: { latitude: number, longitude: number }) {
  getUserPosition((userLat, userLon) => {
    clearRouting();

    window.routingControl = L.Routing.control({
      waypoints: [L.latLng(userLat, userLon), L.latLng(coords.latitude, coords.longitude)],
      routeWhileDragging: false,
      createMarker: function() {
        return L.marker([userLat, userLon], { icon: userIcon });
      }
    } as L.Routing.RoutingControlOptions).addTo(map);

    isNavigating.value = true;
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
  if (!pointStore.selectedIcons.includes('shelter')) {
    iconsOverviewRef.value?.forceIncludeShelter();
    pointStore.updateSelectedIcons([...pointStore.selectedIcons, 'shelter']);
    await pointStore.fetchPointsByIconTypes(pointStore.selectedIcons);
  }
  getUserPosition(async (userLat, userLon) => {
    nearestShelters.value = await pointStore.fetchNearestShelters(userLat, userLon);
    
    if (nearestShelters.value.length === 0) {
      alert('Ingen tilfluktsrom funnet');
      return;
    }
    currentShelterIndex.value = 0;
    viewingNearest.value = true;
    showShelter(currentShelterIndex.value);
  });
}

function showShelter(index: number) {
  selectedPoint.value = nearestShelters.value[index];
  formMode.value = 'view';
  showPointForm.value = true;
  
  // Center map on shelter showing
  map.setView([selectedPoint.value.latitude, selectedPoint.value.longitude], 15);

  removeTempMarker();
  createTempMarker(selectedPoint.value.latitude, selectedPoint.value.longitude);
}

function handleNextShelter() {
  if (nearestShelters.value.length <= 1) return;
  
  currentShelterIndex.value = 
    (currentShelterIndex.value + 1) % nearestShelters.value.length;
  showShelter(currentShelterIndex.value);
}

</script>

<style>
.corner-container {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 2;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-height: calc(100% - 20px); 
  overflow: hidden; 
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
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: var(--bad-red);
  color: var(--white);
  padding: 0 20px;
  border-radius: 5px;
  font-size: var(--font-size-medium);
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
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

@media (max-width: 768px) {
  .crisis-alert {
    font-size: var(--font-size-small);
  }
}
</style>
