<script lang="ts" setup>
import {ref} from 'vue'
import axios from 'axios'
import {useRouter} from 'vue-router'

const code = ref('') // Input field for the verification code
const errorMessage = ref('') // Reference for error message
const router = useRouter() // Router instance for navigation

const confirmCode = async () => { // Function to confirm the verification code
  try {
    await axios.post('http/localhost:8080/api/2fa/confirm', { // API endpoint for confirming the code
      code: code.value // The verification code entered by the user
    })
    router.push('/userhome') // Redirect to the home page after successful confirmation
  } catch (err: any) { // Error handling
    if (err.response && err.response.status === 400) { // Check if the error is a 400 Bad Request
      errorMessage.value = 'Ugyldig eller utløpt kode. '
    } else {
      errorMessage.value = 'En feil oppstod. Prøv igjen'
    }
  }
}
</script>

<template>
  <div class="register-login-wrapper">
    <div class="register-login-container">
      <h1>Totrinnsbekreftelse</h1>
      <p>Vi har sendt deg en e-post med en kode. Skriv inn koden for å bekrefte identiteten din:</p>
      <input v-model="code" type="text" placeholder="Skriv inn kode" />
      <p class="error-message" v-if="errorMessage">{{ errorMessage }}</p>
      <button class="good-button" @click="confirmCode">Bekreft kode</button>
    </div>
  </div>
</template> 
