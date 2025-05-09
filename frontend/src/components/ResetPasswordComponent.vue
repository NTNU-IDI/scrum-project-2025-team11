<script lang="ts" setup>
import {ref} from "vue";
import {validatePassword} from "@/utils/validationService.ts";
import {resetPassword} from "@/api/PasswordService.ts";

const code = ref('')
const passwordInputType = ref('password')
const password = ref('')
const repeatedPassword = ref('')
const showPassword = ref(false)
const errorMessage = ref('')

function attemptResetPassword() {
  if (code.value === '') {
    errorMessage.value = "Vennligst legg inn bekreftelseskoden."
  } else if (!validatePassword(password.value) || !validatePassword(repeatedPassword.value) || password.value === '' ||
      repeatedPassword.value === '') {
    errorMessage.value = "Passordet ditt oppfyller ikke kravene om 8 tegn, med minst én stor bokstav, én liten bokstav," +
        " ett tall og ett spesialtegn."
  } else if (password.value !== repeatedPassword.value && password.value !== '') {
    errorMessage.value = "Passordene dine er ikke like. Vennligst sjekk dette og prøv igjen."
  } else {
    resetPassword(code.value, password.value)
  }
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
    <div class="register-login-wrapper">
      <div class="register-login-container">
        <h1>Tilbakestille passord?</h1>
        <form id="formResetPassword" v-on:submit.prevent>
          <p>
              Du skal ha blitt tilsendt en e-post med en bekreftelseskode. Vennligst skriv inn koden og det nye passordet.
          </p>
          <label>Bekreftelseskode <span class="importantStar">*</span><input type="text" v-model="code"/>  </label>
          <label>Nytt passord <span class="importantStar">*</span><br> <input :type="passwordInputType" v-model="password" id="iptPassword"/> </label>
          <label>Gjenta nytt passord <span class="importantStar">*</span><br><input :type="passwordInputType" v-model="repeatedPassword" id="iptRepeatedPassword" /> </label>
          <label><input type="checkbox" id="cbPassword" v-model="showPassword" @change="changePasswordVisibility"> Vis passord</label>
          <br>
          <p id="error" class="error-message">{{errorMessage}}</p>

          <button class="good-button" type="submit" @click="attemptResetPassword">Sett nytt passord</button>
        </form>
        <p class="register-login-text">
          Skulle du ikke tilbakestille passordet? Gå tilbake til innloggings-siden
          <router-link to="/login" class="link">her</router-link>
        </p>
      </div>
    </div>
  </template>

<style>
#cbPassword {
  width: auto;
}

#formResetPassword {
  font-size: var(--font-size-medium);
}



</style>
  

  