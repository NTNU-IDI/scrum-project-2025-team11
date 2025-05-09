<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />
        <nav class="nav-buttons">
          <a href="/" class="button other"><i class="fa fa-home" aria-hidden="true"></i> Hjem</a>
          <a href="/map" class="button other"><i class="fa fa-map-o" aria-hidden="true"></i> Kart</a>
        </nav>
      </div>

      <div class="header-right">
        <a href="/household" class="button other"><i class="fa fa-medkit" aria-hidden="true"></i> Min beredskap</a> 
        <a href="/" class="button logout" @click.prevent="logOutUser"><i class="fa fa-sign-out" aria-hidden="true"></i> Logg ut</a>
     
      </div>

      <button class="hamburger" @click="toggleMobileMenu"><i class="fa fa-bars" aria-hidden="true"></i> Meny</button>
    </div>

    <nav v-if="isMobileMenuOpen" class="mobile-nav">
      <a href="/" class="button other"><i class="fa fa-home" aria-hidden="true"></i> Hjem</a>
      <a href="/map" class="button other"><i class="fa fa-map-o" aria-hidden="true"></i> Kart</a>
      <a href="/storage" class="button other"><i class="fa fa-medkit" aria-hidden="true"></i> Min beredskap</a>
      <a href="/" class="button logout" @click.prevent="logOutUser"><i class="fa fa-sign-out" aria-hidden="true"></i> Logg ut</a>
    </nav>
  </header>
</template>

<script lang="ts" setup>
import { logOutUser } from '../api/AuthService'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const router = useRouter()
const goToHome = () => router.push('/')
const isMobileMenuOpen = ref(false)
const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}
</script>
