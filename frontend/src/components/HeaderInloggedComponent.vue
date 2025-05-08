<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />
        <a href="/" class="button other"><i class="fa fa-home"></i> Hjem</a> 
          <a href="/map" class="button other"><i class="fa fa-map-o"></i> Kart</a> 
        <nav class="nav-buttons">
          
        </nav>
      </div>

      <div class="header-right">
        <a href="/household" class="button other"><i class="fa fa-medkit"></i> Min beredskap</a> 
        <a href="/" class="button logout" @click.prevent="logOutUser"><i class="fa fa-sign-out"></i> Logg ut</a>
      </div>
    </div>
  </header>
</template>

<script lang="ts" setup>  
import {logOutUser} from '../api/AuthService';
import { useRouter } from 'vue-router'
const router = useRouter()
const goToHome = () => {
  router.push('/')
}
</script>