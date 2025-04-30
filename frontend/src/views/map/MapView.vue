<template>
  <div class="map-page">
    <div class="corner-container">
      <IconsOverview />
      <button class="button" @click="findNearestShelter">Finn nærmeste tilfluktsrom</button>
    </div>

    <div id="map" class="map"></div>

    <div v-if="showCrisisAlert" class="crisis-alert">
      <p><strong>Viktig melding:</strong> Du er i et kriseområde!</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import IconsOverview from '../../components/map/IconsOverview.vue';
import { onMounted, ref } from 'vue';
import { usePointStore, type PointOfInterest } from '@/stores/pointStore';

const pointStore = usePointStore(); 
let map: L.Map;
const showCrisisAlert = ref(false);

declare global {
  interface Window {
    routingControl: any;
  }
}
window.routingControl = null;

// Test data
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

  // Get user location
  getUserLocation(map);
});

function getUserLocation(map: L.Map) {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userLatitude = position.coords.latitude;
        const userLongitude = position.coords.longitude;

        // Add marker
        const userLocationMarker = L.marker([userLatitude, userLongitude])
          .addTo(map)
          .bindPopup("<strong>Din posisjon</strong>")
          .openPopup();
        map.setView([userLatitude, userLongitude], 13);

        // Check if user is in a crisis area
        checkIfInCrisisArea(userLatitude, userLongitude);
      },
      (error) => {
        console.error("Error getting location: ", error);
      }
    );
  } else {
    console.warn("Geolocation is not supported by this browser.");
  }
}

function getEventColor(severity: number) {
  return severity === 1
    ? 'var(--light-orange)'
    : severity === 0
    ? 'var(--yellow)'
    : 'var(--bad-red)';
}

function checkIfInCrisisArea(userLatitude: number, userLongitude: number) {
  testEvents.forEach(event => {
    const distance = calculateDistance(userLatitude, userLongitude, event.latitude, event.longitude);
    
    if (distance <= event.radius) {
      showCrisisAlert.value = true;
    }
  });
}

function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {
  const R = 6371;
  const dLat = toRadians(lat2 - lat1);
  const dLon = toRadians(lon2 - lon1);
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const distance = R * c;
  return distance * 1000;
}

function toRadians(degrees: number): number {
  return degrees * (Math.PI / 180);
}

function addPointsOfInterest(map: L.Map) {
  pointStore.allPoints.forEach(point => {
    const customIcon = L.divIcon({
      html: `<div class="map-icon ${point.iconType}" style="margin: 0;"></div>`,
      className: '',
      iconSize: [20, 20],
      iconAnchor: [10, 10]
    });

    L.marker([point.latitude, point.longitude], {
      icon: customIcon
    }).addTo(map).bindPopup(`<strong>${point.name}</strong><br>${point.description}`);
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

async function findNearestShelter() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(async (position) => {
      const userLatitude = position.coords.latitude;
      const userLongitude = position.coords.longitude;

      await pointStore.fetchShelters();
      const shelters = pointStore.shelters;
      if (shelters.length === 0) return;

      const nearestShelter = getNearestPoint(userLatitude, userLongitude, shelters);
      
      if (nearestShelter) {
        // Remove previous route
        if (window.routingControl) {
          map.removeControl(window.routingControl);
        }
        // Show route
        L.Routing.control({
          waypoints: [
            L.latLng(userLatitude, userLongitude),
            L.latLng(nearestShelter.latitude, nearestShelter.longitude)
          ],
          routeWhileDragging: false,
        }).addTo(map);
      }
    });
  }
}

function getNearestPoint(
  userLatitude: number,
  userLongitude: number,
  points: PointOfInterest[]
): PointOfInterest | null {
  let nearestPoint: PointOfInterest | null = null;
  let minDistance = Infinity;

  points.forEach(point => {
    const distance = calculateDistance(userLatitude, userLongitude, point.latitude, point.longitude);
    if (distance < minDistance) {
      minDistance = distance;
      nearestPoint = point;
    }
  });

  return nearestPoint;
}
</script>

<style>
.corner-container {
  position: absolute;
  top: 30px;
  left: 20px;
  z-index: 2;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.button {
  background-color: var(--dark-blue);
  color: var(--white);
  font-size: var(--font-size-small);
  padding: 13px;
}

.button:hover {
  background-color: var(--darkest-blue);
}

.map-page {
  display: flex;
  height: 100vh; 
}

.map {
  height: 100%;
  width: 100%;
  z-index: 0;
}

.crisis-alert {
  position: fixed;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  background-color: var(--bad-red);
  color: var(--white);
  padding: 10px 20px;
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
    padding: 0 20px;
    font-size: var(--font-size-small);
  }
}
</style>
