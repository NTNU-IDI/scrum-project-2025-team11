<script lang="ts" setup>
import {ref} from "vue";
import {validateEmail} from "@/utils/validationService.ts";
import {sendEmailForReset} from "@/api/PasswordService.ts";
import {useToast} from "vue-toast-notification";

const email = ref('')
const $toast = useToast()

function sendEmail() {
  if (validateEmail(email.value)) {
    sendEmailForReset(email.value)
  } else {
    let instance = $toast.info("Kunne ikke sende bekreftelseskode grunnet ugyldig email.")
  }
}

</script>

<template>
    <div class="register-login-wrapper">
      <div class="register-login-container">
        <h1>Tilbakestille passord?</h1>
        <p>
            Skriv inn e-postadressen til kontoen din for å få tilsendt en bekreftelseskode. 
        </p>
        <form v-on:submit.prevent>
          <input type="text" v-model="email" placeholder="Skriv e-postadresse her" />
          <button class="good-button" type="submit" @click="sendEmail">Send kode</button>
        </form>
        <p class="register-login-text">
          Skulle du ikke tilbakestille passordet? Gå tilbake til innloggings-siden
          <router-link to="/login" class="link">her</router-link>
        </p>
      </div>
    </div>
  </template>

  