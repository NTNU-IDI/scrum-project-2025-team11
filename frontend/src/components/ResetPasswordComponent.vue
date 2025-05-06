<template>
    <div class="register-login-wrapper">
      <div class="register-login-container">
        <h1>Tilbakestille passord?</h1>
        <p>
            Du skal ha fått tilsendt en e-post med en bekreftelseskode. Skriv koden inn nedenfor:
        </p>
        <input type="text" placeholder="Bekreftelseskode" />
        <p>
            Skriv inn nytt passord:
        </p>
        <input type="text" placeholder="Nytt passord" />
        <input type="text" placeholder="Gjenta nytt passord" />
        
        <button class="good-button">Sett nytt passord</button>
        
        <p class="register-login-text">
          Har du ikke en bruker?
          <router-link to="/register" class="link">Registrer deg her</router-link>
        </p>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  </script>
  