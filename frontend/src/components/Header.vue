<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome"/>
        <nav class="nav-buttons">
          <router-link to="/" class="button other"><i class="fa fa-home" aria-hidden="true"></i> Hjem</router-link>
          <router-link to="/map" class="button other"><i class="fa fa-map-o" aria-hidden="true"></i> Kart</router-link>
        </nav>
      </div>

      <nav class="header-right">
        <router-link to="/login" class="button login"><i class="fa fa-sign-in" aria-hidden="true"></i> Logg inn / <i class="fa fa-user-plus" aria-hidden="true"></i> Registrer deg</router-link>
      </nav>
      <button class="hamburger" @click="toggleMobileMenu"><i class="fa fa-bars" aria-hidden="true"></i> Meny</button>
    </div>

    <nav v-if="isMobileMenuOpen" class="mobile-nav">
      <router-link to="/" class="button other"><i class="fa fa-home" aria-hidden="true"></i> Hjem</router-link>
      <router-link to="/map" class="button other"><i class="fa fa-map-o" aria-hidden="true"></i> Kart</router-link>
      <router-link to="/login" class="button login"><i class="fa fa-sign-in" aria-hidden="true"></i> Logg inn / <i class="fa fa-user-plus" aria-hidden="true"></i> Registrer</router-link>
    </nav>
  </header>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const router = useRouter()
const goToHome = () => router.push('/')
const isMobileMenuOpen = ref(false)
const toggleMobileMenu = () => isMobileMenuOpen.value = !isMobileMenuOpen.value
</script>
