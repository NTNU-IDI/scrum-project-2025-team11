<template>
  <div class="map-overview-box">
    <div class="map-overview-box-header" @click="isCollapsed = !isCollapsed">
      <h1 class="title-map">Ikoner</h1>
      <span class="toggle-icon">
        {{ isCollapsed ? '▼' : '▲' }}
      </span>
    </div>

    <transition name="fade">
      <ul v-show="!isCollapsed" class="map-overview-box-descriptions">
        <li><span class="map-icon shelter"></span> Tilfluktsrom</li>
        <li><span class="map-icon assembly_point"></span> Møteplass</li>
        <li><span class="map-icon medical"></span> Medisinsk hjelp</li>
      </ul>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const isCollapsed = ref(false);
</script>

<style scoped>
</style>