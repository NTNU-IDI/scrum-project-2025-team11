<template>
  <div class="icons-box">
    <h1 class="title-map">Ikoner</h1>
    <ul class="descriptions">
      <li><span class="map-icon shelter"></span> Tilfluktsrom</li>
      <li><span class="map-icon assembly_point"></span> Møteplass</li>
      <li><span class="map-icon medical"></span> Medisinks hjelp</li>
      <li><span class="map-icon danger severe"></span> Alvorlig kriseberørt område</li>
      <li><span class="map-icon danger moderate"></span> Moderat kriseberørt område</li>
      <li><span class="map-icon danger mild"></span> Lett kriseberørt område</li>
    </ul>
  </div>
</template>

<script setup lang="ts">
</script>


<style scoped>
.icons-box { 
  background: var(--white);
  padding: 1rem 1.5rem 1rem 1rem;
  border-radius: 10px;
  z-index: 1; 
  display: flex;
  flex-direction: column;
}

.descriptions {
  list-style: none;
  padding: 0;
  margin: 0;
}

.icons-box li {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
  font-size: var(--font-size-small);
}

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

@media (max-width: 768px) {
  .icons-box li {
    font-size: var(--font-size-xsmall);
    margin-bottom: 0.3rem;
  }

  .title {
    font-size: var(--font-size-large);
  }
}
</style>