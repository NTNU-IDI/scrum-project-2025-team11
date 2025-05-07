<script setup lang="ts">
import {login} from "@/api/AuthService.ts";
import {ref} from "vue";

const username = ref('')
const password = ref('')
const errorMessage = ref('');

function attemptLogin() {
  errorMessage.value = ''
  if (simpleValidation()) {
    login(username.value, password.value)
  }
}

function simpleValidation() {
  if (username.value === '') {
    errorMessage.value = "Vennlgist fyll ut brukernavn."
    return false
  } else if (password.value === '') {
    errorMessage.value = "Vennligst full ut passord."
    return false
  }
  return true
}

</script>

<template>
  <div class="register-login-container">
    <h1>Logg inn</h1>
    <form v-on:submit.prevent>
      <input type="text" v-model="username" placeholder="Brukernavn"/>
      <input type="password" v-model="password" placeholder="Passord"/>
      <button class="good-button" type="submit" @click="attemptLogin">Logg inn</button>
      <p id="error" class="error-message">{{errorMessage}}</p>

    </form>
    <p class="register-login-text">
      Har du ikke en bruker?
      <router-link to="/register" class="link">Registrer deg her</router-link>
    </p>
    <p class="register-login-text">
      Har du glemt passord?
      <router-link to="/forgot" class="link">Tilbakestill passord</router-link>
    </p>
  </div>
</template>

