<script setup lang="ts">

</script>

<template>
  <div id="divRegister" class="register-login-container">
    <h1>Registrer bruker</h1>
    <form v-on:submit.prevent>
      <input type="text" placeholder="Brukernavn"> <br>
      <input type="email" placeholder="Epost"> <br>
      <input type="password" placeholder="Passord"> <br>
      <input type="password" placeholder="Gjenta passord"> <br>
      <button class="good-button" type="submit"> Registrer </button>
    </form>
    <p class="register-login-text">
      Har du allerede en bruker? <a href="#" class="link">Logg inn her</a>
    </p>
  </div>
</template>

<style scoped>

</style>