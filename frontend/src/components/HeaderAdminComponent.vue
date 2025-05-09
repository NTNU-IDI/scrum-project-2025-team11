<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />
        <a href="/map" class="button other"><i class="fa fa-map-o"></i> Kart</a> 
      </div>

      <div class="header-right">
        <a href="/" class="button logout" @click.prevent="logOut"><i class="fa fa-sign-out"></i> Logg ut</a>
      </div>
    </div>
  </header>
</template>


<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goToHome = () => {
  router.push('/')
}

import {logOutUser} from '../api/AuthService';

async function logOut() {
  await logOutUser()
}
</script>