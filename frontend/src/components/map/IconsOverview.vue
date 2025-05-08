<template>
  <div class="map-overview-box">
    <div class="map-overview-box-header" @click="isCollapsed = !isCollapsed">
      <h1 class="title-map">Ikoner</h1>
      <span class="toggle-icon">
        {{ isCollapsed ? '▼' : '▲' }}
      </span>
    </div>

    <transition name="fade">
      <ul v-show="!isCollapsed" class="map-overview-details">
        <li class="map-li">
          <input type="checkbox" id="shelter" value="shelter" v-model="selectedFilters" />
          <label for="shelter"><span class="map-icon shelter"></span> Tilfluktsrom</label>
        </li>
        <li class="map-li">
          <input type="checkbox" id="assembly_point" value="assembly_point" v-model="selectedFilters" />
          <label for="assembly_point"><span class="map-icon assembly_point"></span> Møteplass</label>
        </li>
        <li class="map-li">
          <input type="checkbox" id="medical" value="medical" v-model="selectedFilters" />
          <label for="medical"><span class="map-icon medical"></span> Medisinsk hjelp</label>
        </li>
      </ul>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
import { usePointStore } from '@/stores/pointStore';

const isCollapsed = ref(false);
const selectedFilters = ref<string[]>(['shelter', 'assembly_point', 'medical']);
const pointStore = usePointStore()

watch(selectedFilters, (newIcons) => {
  pointStore.updateSelectedIcons(newIcons);
});

onMounted(() => {
  pointStore.updateSelectedIcons(selectedFilters.value);
  pointStore.startPolling();
});

const forceIncludeShelter = () => {
  if (!selectedFilters.value.includes('shelter')) {
    selectedFilters.value = [...selectedFilters.value, 'shelter'];
  }
};

defineExpose({
  forceIncludeShelter
});
</script>

<style scoped>
.map-overview-details input[type="checkbox"] {
  margin: 0;
  flex-shrink: 0;
  width: auto;
  padding: 0;
}

.map-overview-details label {
  display: flex;
  align-items: center;
  margin-left: 0.5rem;
  cursor: pointer;
  flex-grow: 1;
  font-size: var(--font-size-xsmall);
}
</style>