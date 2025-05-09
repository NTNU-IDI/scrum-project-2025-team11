<script lang="ts" setup>
import {ref} from 'vue'
import axios from 'axios'
import {useRouter} from 'vue-router'
import {confirm2fa} from "@/api/AuthService.ts";


const code = ref('') // Input field for the verification code
const errorMessage = ref('') // Reference for error message
const router = useRouter() // Router instance for navigation

function confirmCode() {
  errorMessage.value = ''
  if (validateCode()) {
    confirm2fa(code.value)
  }
}

function validateCode() {
  if(code.value === '') {
    errorMessage.value = 'Vennligst skriv inn koden'
    return false;
  }
  return true;
}
</script>

<template>
  <div class="register-login-wrapper" id="authWrapper">
    <div class="register-login-container">
      <form v-on:submit.prevent>
      <h1>Totrinnsbekreftelse</h1>
      <p>Vi har sendt deg en e-post med en kode. Skriv inn koden for å bekrefte identiteten din:</p>
      <input v-model="code" type="text" placeholder="Skriv inn kode" />
      <p class="error-message" v-if="errorMessage">{{ errorMessage }}</p>
      <button class="good-button" type="submit" @click="confirmCode">Bekreft kode</button>
      </form>
    </div>
  </div>
</template> 

<style>
#authWrapper {
  height: 100vh;
  padding: 0rem;
}
</style>