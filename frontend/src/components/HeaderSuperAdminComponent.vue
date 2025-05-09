<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome"/>
      </div>

      <div class="header-right">
        <router-link to="/" class="button logout" @click.prevent="logOut"><i class="fa fa-sign-out" aria-hidden="true"></i> Logg ut</router-link>

      </div>

      <button class="hamburger" @click="toggleMobileMenu"><i class="fa fa-bars" aria-hidden="true"></i> Meny</button>
    </div>

    <nav v-if="isMobileMenuOpen" class="mobile-nav">
      <router-link to="/" class="button logout" @click.prevent="logOutUser"><i class="fa fa-sign-out" aria-hidden="true"></i> Logg ut</router-link>
    </nav>
  </header>
</template>
  
<script lang="ts" setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import {logOutUser} from '../api/AuthService';

const router = useRouter()
const goToHome = () => {
  router.push('/')
}

async function logOut() {
  await logOutUser()
}
const isMobileMenuOpen = ref(false)
const toggleMobileMenu = () => isMobileMenuOpen.value = !isMobileMenuOpen.value
</script>
  
  