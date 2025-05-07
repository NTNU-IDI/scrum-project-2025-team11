<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToAdminHome" />
      </div>

      <div class="header-right">
        <img src="../assets/ikon/map-trifold-fill.png" alt="Kart" class="icon" @click="goToMap" data-tooltip="Til kartet" />
        <a href="/" class="button logout" @click.prevent="goToLogout">Logg ut</a>
      </div>
    </div>
  </header>
</template>


<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goToAdminHome = () => {
  router.push('/adminhome')
}
const goToLogout = () => {
  router.push('/')
}
const goToMap = () => {
  router.push('/map')
}

</script>

