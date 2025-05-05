<template>
  <div class="map-overview-box">
    <div class="map-overview-box-header" @click="isCollapsed = !isCollapsed">
      <h1 class="title-map">Ikoner</h1>
      <span class="toggle-icon">
        {{ isCollapsed ? '▼' : '▲' }}
      </span>
    </div>

    <transition name="fade">
      <ul v-show="!isCollapsed" class="checkbox-icons-container">
        <li>
          <input type="checkbox" id="shelter" value="shelter" v-model="selectedIcons" />
          <label for="shelter"><span class="map-icon shelter"></span> Tilfluktsrom</label>
        </li>
        <li>
          <input type="checkbox" id="assembly_point" value="assembly_point" v-model="selectedIcons" />
          <label for="assembly_point"><span class="map-icon assembly_point"></span> Møteplass</label>
        </li>
        <li>
          <input type="checkbox" id="medical" value="medical" v-model="selectedIcons" />
          <label for="medical"><span class="map-icon medical"></span> Medisinsk hjelp</label>
        </li>
      </ul>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { usePointStore } from '@/stores/pointStore';

const isCollapsed = ref(false);
const selectedIcons = ref<string[]>([])

const pointStore = usePointStore()

watch(selectedIcons, (newIcons) => {
  // TODO: Call PointStore -> Calls Backend
})
</script>

<style scoped>
.checkbox-icons-container {
  list-style: none;
  padding: 0;
  margin: 0;
  padding-top: 7px;
}

.checkbox-icons-container li {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
  width: 100%;
}

.checkbox-icons-container input[type="checkbox"] {
  margin: 0;
  flex-shrink: 0;
  width: auto;
  padding: 0;
}

.checkbox-icons-container label {
  display: flex;
  align-items: center;
  margin-left: 0.5rem;
  cursor: pointer;
  flex-grow: 1;
  font-size: var(--font-size-xsmall);
}

@media (max-width: 768px) {
    .checkbox-icons-container label {
      display: flex;
      font-size: var(--font-size-xsmall);
  }
}
</style>