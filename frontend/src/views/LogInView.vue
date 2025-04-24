<template>
  <div class="min-h-screen bg-dark-blue flex items-center justify-center font-Roboto">
    <div class="bg-white px-10 py-12 rounded-md shadow-md w-[400px]">
      <div class="mb-6">
        <h1 class="text-2xl font-semibold text-dark-blue">Logg inn</h1>
      </div>
      <form @submit.prevent="handleLogin" class="flex flex-col space-y-4">
        <input
          v-model="username"
          type="text"
          placeholder="Brukernavn"
          class="px-4 py-2 bg-grey rounded focus:outline-none text-dark-blue"
        />
        <input
          v-model="password"
          type="password"
          placeholder="Passord"
          class="px-4 py-2 bg-grey rounded focus:outline-none text-dark-blue"
        />
        <button
          type="submit"
          class="bg-good-green hover:bg-green-700 text-white py-2 rounded transition duration-200"
        >
          Logg inn
        </button>
      </form>
      <p class="mt-6 text-sm text-center text-dark-blue">
        Har du ikke en bruker? <a href="#" class="text-bad-red">Registrer deg her</a>
      </p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const username = ref('')
const password = ref('')

const handleLogin = () => {
  console.log('Login attempt with:', username.value, password.value)
  // TODO: implement actual auth logic
}
</script>
