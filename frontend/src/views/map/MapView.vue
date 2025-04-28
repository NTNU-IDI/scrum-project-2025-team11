<template>
  <div class="map-page">
    <IconsOverview />
    <div id="map" class="map"></div>

    <div v-if="showCrisisAlert" class="crisis-alert">
      <p><strong>Viktig melding:</strong> Du er i et kriseområde!</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import IconsOverview from '../../components/map/IconsOverview.vue';
import { onMounted, ref } from 'vue';
const showCrisisAlert = ref(false);

// Test data
const testPointsOfInterest = [
  {
    id: 1,
    name: 'Tilfluktsrom Sentrum',
    icon_type: 'star',
    description: 'Offentlig tilfluktsrom i sentrum.',
    latitude: 63.432,
    longitude: 10.393
  },
  {
    id: 2,
    name: 'Møteplass Elgeseter',
    icon_type: 'meetup',
    description: 'Samleplass ved Elgeseter gate.',
    latitude: 63.434,
    longitude: 10.399,
  }
];

const testEvents = [
  {
    id: 1,
    name: 'Kriseområde Ila',
    description: 'Område berørt av hendelse.',
    icon_type: 'danger',
    time_start: new Date('2025-04-25'),
    time_end: new Date('2025-04-26'),
    latitude: 63.42,
    langtitude: 10.38,
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
    langtitude: 10.39,
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
    langtitude: 10.42,
    radius: 800,
    severity: 0 
  }
];

onMounted(() => {
  // Init map
  const map = L.map('map', {
    zoomControl: false
  }).setView([63.4305, 10.3951], 12);
  L.control.zoom({ position: 'topright' }).addTo(map);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

  // Add points of interest
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

function checkIfInCrisisArea(userLatitude: number, userLongitude: number) {
  testEvents.forEach(event => {
    const distance = calculateDistance(userLatitude, userLongitude, event.latitude, event.langtitude);
    
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
  testPointsOfInterest.forEach(point => {
    const customIcon = L.divIcon({
      html: `<div class="icon ${point.icon_type}" style="margin: 0;"></div>`,
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
  testEvents.forEach(event => {
    let circleColor = 'var(--bad-red)'; 
    let fillColor = 'var(--bad-red)';

    if (event.severity === 1) {
      circleColor = 'var(--light-orange)';
      fillColor = 'var(--light-orange)';
    } else if (event.severity === 0) {
      circleColor = 'var(--yellow)';
      fillColor = 'var(--yellow)';
    }

    L.circle([event.latitude, event.langtitude], {
      color: circleColor,
      fillColor: fillColor,
      weight: 1,
      radius: event.radius,
      fillOpacity: 0.3 
    }).addTo(map).bindPopup(`<strong>${event.name}</strong><br>${event.description}`);
  });
}
</script>

<style>
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

@media (max-width: 768px) {
  .crisis-alert {
    padding: 0 20px;
    font-size: var(--font-size-small);
  }
}
</style>
