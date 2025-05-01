<script setup lang="ts">
import LogInComponent from '@/components/LogInComponent.vue';
import RegisterComponent from "@/components/RegisterComponent.vue";
import AuthComponent from '@/components/AuthComponent.vue';
</script>

<template>
  <div class="register-login-wrapper">
    <LogInComponent/>
  </div>

  <div class="register-login-wrapper">
    <RegisterComponent />
  </div>

  <div class="auth-view">
    <AuthComponent />
  </div>
</template>
