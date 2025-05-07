<template>
    <div class="register-login-wrapper">
      <div class="register-login-container">
        <h1>Tilbakestille passord?</h1>
        <p>
            Skriv inn e-postadressen til kontoen din for å få tilsendt en bekreftelseskode. 
        </p>
        <input type="text" placeholder="Skriv e-postadresse her" />
        <button class="good-button">Send kode</button>
        <p class="register-login-text">
          Har du ikke en bruker?
          <router-link to="/register" class="link">Registrer deg her</router-link>
        </p>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  </script>
  