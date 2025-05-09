<template>
  <div class="point-card">
    <span class="close-icon" @click="$emit('close')">×</span>

    <div class="point-card-content">
      <p class="title-map">Bestem type:</p>
      <div class="button-container">
        <button @click="$emit('add-point')" class="map-button">Legg til punkt</button>
        <button @click="$emit('add-event')" class="map-button">Legg til hendelse</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.button-container {
  display: flex;
  flex-direction: column;
  gap: 5px;
  margin-top: 10px;
}

@media (max-width: 480px) {
}
</style>
