<template>
  <header class="header">
    <div class="header-container">
      <div class="header-icons">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" data-tooltip="Til hjemmesiden" />
        <img src="../assets/ikon/map-trifold-fill.png" alt="Kart" class="icon" @click="goToMap" data-tooltip="Til kartet" />
        <img src="../assets/ikon/lightbulb-fill.png" alt="Beredskap" class="icon" @click="goToInformation" data-tooltip="Beredskap" />
      </div>

      <nav class="nav-buttons">
        <a href="/login" class="button login">Logg inn</a>
      </nav>
    </div>
  </header>
</template>



<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goToMap = () => {
  router.push('/map')
}
const goToHome = () => {
  router.push('/')
}
const goToLogin = () => {
  router.push('/login')
}
const goToInformation = () => {
  router.push('/information')
}
</script>

