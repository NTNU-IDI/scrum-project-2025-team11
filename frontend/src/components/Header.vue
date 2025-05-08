<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome"/>
        <nav class="nav-buttons">
          <a href="/" class="button other"><i class="fa fa-home" aria-hidden="true"></i> Hjem</a> 
          <a href="/map" class="button other"><i class="fa fa-map-o" aria-hidden="true"></i> Kart</a> 
        </nav>
      </div>

      <nav class="header-right">
        <a href="/login" class="button login"><i class="fa fa-sign-in" aria-hidden="true"></i> Logg inn / <i class="fa fa-user-plus" aria-hidden="true"></i> Registrer deg</a>
      </nav>
      <button class="hamburger" @click="toggleMobileMenu"><i class="fa fa-bars" aria-hidden="true"></i> Meny</button>
    </div>

    <nav v-if="isMobileMenuOpen" class="mobile-nav">
      <a href="/" class="button other"><i class="fa fa-home" aria-hidden="true"></i> Hjem</a>
      <a href="/map" class="button other"><i class="fa fa-map-o" aria-hidden="true"></i> Kart</a>
      <a href="/login" class="button login">
        <i class="fa fa-sign-in" aria-hidden="true"></i> Logg inn / 
        <i class="fa fa-user-plus" aria-hidden="true"></i> Registrer
      </a>
    </nav>
  </header>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const router = useRouter()
const goToHome = () => router.push('/')

const isMobileMenuOpen = ref(false)
const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}
</script>
