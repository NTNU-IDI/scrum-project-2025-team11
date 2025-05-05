<template>
  <div class="map-overview-box">
    <div class="map-overview-box-header" @click="isCollapsed = !isCollapsed">
      <h1 class="title-map">Hendelser</h1>
      <span class="toggle-icon">
        {{ isCollapsed ? '▼' : '▲' }}
      </span>
    </div>

    <transition name="fade">
      <ul v-show="!isCollapsed" class="map-overview-box-descriptions">
        <li><span class="map-icon danger severe"></span> Alvorlig kriseberørt område</li>
        <li><span class="map-icon danger moderate"></span> Moderat kriseberørt område</li>
        <li><span class="map-icon danger mild"></span> Lett kriseberørt område</li>
      </ul>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const isCollapsed = ref(false);
</script>


<style scoped>
.map-icon.danger {
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: inline-block;
}

.map-icon.danger.severe {
  background-color: var(--bad-red-opacity);
  border: 1px solid var(--bad-red);
}

.map-icon.danger.moderate {
  background-color: var(--light-orange-opacity);
  border: 1px solid var(--light-orange);
}

.map-icon.danger.mild {
  background-color: var(--yellow-opacity);
  border: 1px solid var(--yellow);
}
</style>