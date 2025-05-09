<script setup lang="ts">
import {login} from "@/api/AuthService.ts";
import {ref} from "vue";

const username = ref('')
const password = ref('')
const passwordInputType = ref('password')
const showPassword = ref(false)
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
    errorMessage.value = "Vennligst fyll ut passord."
    return false
  }
  return true
}

function changePasswordVisibility() {
  if(showPassword.value === false) {
    passwordInputType.value = 'password'
  } else {
    passwordInputType.value = 'text'
  }
}

</script>

<template>
  <div class="register-login-container">
    <h1>Logg inn</h1>
    <form v-on:submit.prevent>
      <label>Brukernavn <span class="importantStar">*</span><input type="text" v-model="username"/></label>
      <label>Passord <span class="importantStar">*</span><input :type="passwordInputType" v-model="password" id="iptPassword"/></label>
      <label><input type="checkbox" id="cbPassword" v-model="showPassword"> Vis passord</label>
      <br> <br>
      <button class="good-button" type="submit" @click="attemptLogin">Logg inn</button>
    </form>
    <p id="error" class="error-message">{{errorMessage}}</p>

    <p class="register-login-text">
      Har du ikke en bruker?
      <router-link to="/register" class="link" id="linkToRegister">Registrer deg her</router-link>
    </p>
    <p class="register-login-text">
      Har du glemt passord?
      <router-link to="/forgot" class="link" id="linkToForgot">Tilbakestill passord</router-link>
    </p>
  </div>
</template>

