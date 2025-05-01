<template>
    <div class="register-login-wrapper">
      <div class="register-login-container">
        <h1>Totrinnsbekreftelse</h1>
        <p>
          Vi har sendt deg en e-post med en kode. Skriv inn koden for å bekrefte identiteten din.
        </p>
        <input type="text" placeholder="Skriv koden her" />
        <button class="good-button">Bekreft kode</button>
        <p class="register-login-text">
          Har du ikke en bruker?
          <router-link to="/register" class="link">Registrer deg her</router-link>
        </p>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  </script>
  