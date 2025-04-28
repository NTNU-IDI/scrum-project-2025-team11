<script setup lang="ts">
</script>

<template>
  <div class="register-login-container">
    <h1>Logg inn</h1>
    <input type="text" placeholder="Brukernavn"/>
    <input type="password" placeholder="Passord"/>
    <button class="good-button">Logg inn</button>
    <p class="register-login-text">
      Har du ikke en bruker?
      <router-link to="/register" class="link">Registrer deg her</router-link>
    </p>
  </div>
</template>

