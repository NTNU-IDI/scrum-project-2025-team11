<template>
  <div class="map-page">
    <div class="icons-box">
      <h1>Ikoner</h1>
      <ul>
        <li><span class="icon star"></span> Tilfluktsrom</li>
        <li><span class="icon danger"></span> Kriseberørt område</li>
        <li><span class="icon meetup"></span> Møteplass</li>
      </ul>
    </div>
    <div id="map" class="map"></div>
  </div>
</template>

<script lang="ts">
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { defineComponent, onMounted } from 'vue';

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
    longitude: 10.399
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
    radius: 1000
  }
];

export default defineComponent({
  name: 'CrisisMap',
  setup() {
    onMounted(() => {
      // Init map
      const map = L.map('map', {
        zoomControl: false
      }).setView([63.4305, 10.3951], 12);
      L.control.zoom({ position: 'topright' }).addTo(map);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
      }).addTo(map);

      // Add interest points
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

      // Add events
      testEvents.forEach(event => {
          L.circle([event.latitude, event.langtitude], {
            color: 'var(--bad-red)',
            fillColor: 'var(--bad-red)',
            weight: 1,
            radius: event.radius
          }).addTo(map).bindPopup(`<strong>${event.name}</strong><br>${event.description}`);
        });
    });
  }
});
</script>

<style>
.map-page {
  display: flex;
  height: 100vh; 
}

.icons-box {
  position: absolute;  
  top: 10px;          
  left: 10px;       
  background: var(--white);
  padding: 1rem 1.5rem 1rem 1rem;
  border-radius: 10px;
  font-size: 0.9rem;
  color: var(--darkest-blue);
  z-index: 1; 
  display: flex;
  flex-direction: column;
}

.icons-box ul {
  list-style: none;
  padding: 0;
}

.icons-box li {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}

.icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 0.5rem;
}

.icon.star {
  background: url('/yellow-star-fill-border.svg') no-repeat center center / contain;
}

.icon.danger {
  background-color: var(--bad-red-opacity);
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: inline-block;
  border: 1px solid var(--bad-red);
}

.icon.meetup {
  background: url('/lightest-blue-triangle-fill-border.svg') no-repeat center center / contain;
}

.map {
  height: 100%;
  width: 100%;
  z-index: 0;
}
</style>
