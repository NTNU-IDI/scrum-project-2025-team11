<template>
  <div class="layout-map-page">
    <Header />
    <div class="map-page">
      <div class="corner-container">
        <IconsOverview />
        <EventsOverview />
        <PointForm 
          v-if="showPointForm" 
          :selectedPoint="selectedPoint" 
          :mode="formMode"
          @close="closePointForm" 
          @coordinates-updated="updateMarkerPosition"
          @navigate="handleNavigation"
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
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import IconsOverview from '../../components/map/IconsOverview.vue';
import EventsOverview from '../../components/map/EventsOverview.vue';
import PointForm from '../../components/map/PointView.vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { onMounted, ref } from 'vue';
import { usePointStore } from '@/stores/pointStore';
import type { PointOfInterest } from "@/types/PointOfInterest";
import { calculateDistance, getEventColor } from '@/utils/geoService';
import {useUserStore} from "@/stores/userStore.ts";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const {role} = storeToRefs(userStore)
const pointStore = usePointStore(); 
const showCrisisAlert = ref(false);
const showPointForm = ref(false);
const formMode = ref<'edit' | 'create' | 'view'>('create');
const selectedPoint = ref<PointOfInterest>({
  id: 0,
  name: '',
  description: '',
  iconType: '',
  latitude: 0,
  longitude: 0,
});

let map: L.Map;
let temporaryMarker: L.Marker | null = null;

declare global {
  interface Window {
    routingControl: any;
  }
}
window.routingControl = null;

// TODO: Retrieve event data from backend
type Event = {
  id: number;
  name: string;
  description: string;
  icon_type: string;
  time_start: Date;
  time_end: Date;
  latitude: number;
  longitude: number;
  radius: number;
  severity: number;
};

const testEvents = [
  {
    id: 1,
    name: 'Kriseområde Ila',
    description: 'Område berørt av hendelse.',
    icon_type: 'danger',
    time_start: new Date('2025-04-25'),
    time_end: new Date('2025-04-26'),
    latitude: 63.42,
    longitude: 10.38,
    radius: 1000,
    severity: 2 
  },
  {
    id: 2,
    name: 'Kriseområde Moholt',
    description: 'Område med moderate hendelser.',
    icon_type: 'danger',
    time_start: new Date('2025-04-26'),
    time_end: new Date('2025-04-27'),
    latitude: 63.43,
    longitude: 10.39,
    radius: 1500,
    severity: 1 
  },
  {
    id: 3,
    name: 'Kriseområde Tiller',
    description: 'Liten hendelse uten alvorlige konsekvenser.',
    icon_type: 'danger',
    time_start: new Date('2025-04-26'),
    time_end: new Date('2025-04-27'),
    latitude: 63.42,
    longitude: 10.42,
    radius: 800,
    severity: 0 
  }
];

onMounted(async () => {
  // Init map
  map = L.map('map', {
    zoomControl: false
  }).setView([63.4305, 10.3951], 12);
  L.control.zoom({ position: 'topright' }).addTo(map);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

  // Add points of interest
  await pointStore.fetchAllPoints();
  addPointsOfInterest(map);

  // Add events
  addEvents(map);

  // Get user location and set marker
  getUserPosition((lat, lon) => {
  L.marker([lat, lon]).addTo(map).bindPopup("Din posisjon").openPopup();
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

  // Get user position
  navigator.geolocation.getCurrentPosition(
    pos => callback(pos.coords.latitude, pos.coords.longitude),
    err => console.error("Error getting location: ", err)
  );
}

function checkIfInCrisisArea(userLatitude: number, userLongitude: number) {
  testEvents.forEach(event => {
    const distance = calculateDistance(userLatitude, userLongitude, event.latitude, event.longitude);
    
    if (distance <= event.radius) {
      showCrisisAlert.value = true;
    }
  });
}

function addPointsOfInterest(map: L.Map) {
  pointStore.allPoints.forEach(point => {
    const customIcon = L.divIcon({
      // Set class based on point type
      html: `<div class="map-icon ${point.iconType}" style="margin: 0;"></div>`,
      className: '',
      iconSize: [20, 20],
      iconAnchor: [10, 10]
    });

    // Add point to map
    L.marker([point.latitude, point.longitude], {
      icon: customIcon
    }).addTo(map)
      .on('click', () => {
        selectedPoint.value = { ...point };

        // ADMIN: Edit on icon click
        if (role.value === 'admin') {
          removeTempMarker();
          formMode.value = 'edit';
          showPointForm.value = true;

          // R/NR USERS View details
        } else {
          formMode.value = 'view';
          showPointForm.value = true;        
        }
    });
  });
}

function addEvents(map: L.Map) {
  testEvents.forEach(({ latitude, longitude, radius, severity, name, description }) => {
    const color = getEventColor(severity);
    L.circle([latitude, longitude], {
      color,
      fillColor: color,
      weight: 1,
      radius,
      fillOpacity: 0.3
    }).addTo(map).bindPopup(`<strong>${name}</strong><br>${description}`);
  });
}

function handleNavigation(coords: { latitude: number, longitude: number }) {
  getUserPosition((userLat, userLon) => {
    if (window.routingControl) map.removeControl(window.routingControl);

    window.routingControl = L.Routing.control({
      waypoints: [L.latLng(userLat, userLon), L.latLng(coords.latitude, coords.longitude)],
      routeWhileDragging: false,
    }).addTo(map);
  });
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

@media (max-width: 768px) {
  .crisis-alert {
    font-size: var(--font-size-small);
  }
}
</style>
