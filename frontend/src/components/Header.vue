<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome"/>
        <nav class="nav-buttons">
          <a href="/" class="button other"><i class="fa fa-home"></i> Hjem</a> 
          <a href="/map" class="button other"><i class="fa fa-map-o"></i> Kart</a> 
        </nav>
      </div>

      <nav class="header-right">
        <a href="/login" class="button login"><i class="fa fa-sign-in"></i> Logg inn</a> 
      </nav>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goToHome = () => {
  router.push('/')
}

</script>

