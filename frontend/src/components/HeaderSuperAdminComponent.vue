<template>
    <header class="header">
      <div class="header-container">
        <div class="header-left">
          <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToSuperAdmin" />
        </div>
  
        <div class="header-right">
            <a href="/" class="button logout" @click.prevent="logOutUser">Logg ut</a>
        </div>
      </div>
    </header>
  </template>
  
  <script lang="ts" setup>
  import { useRouter } from 'vue-router'
  const router = useRouter()
  const goToSuperAdmin = () => {
    router.push('/superadmin')
  }
  
  import {logOutUser} from '../api/AuthService';
  </script>
  
  