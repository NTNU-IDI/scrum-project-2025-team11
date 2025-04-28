<script setup lang="ts">
import {ref, watch} from 'vue'
import axios from 'axios'

const postalCode = ref('');
let errorMessage = ref('');

/*
How to get the relevant information from this request (has to be inside a method):
  const location = await getLocationDataFromAdressAndPostalCode('Katteberget 17', '3721')
  const longitude = location.lon
  const latitude = location.lat
  const displayName = location.display_name
  const displayNameArray = displayName.split(',')
  const city = placeArr[placeArr.length - 4];

The things to retrieve for backend are: longitude, latitude and city
 */
async function getLocationDataFromAdressAndPostalCode(address: string, postalCode: string) {
  const response = await axios.get(`https://nominatim.openstreetmap.org/search?street=${address}&postalcode=${postalCode}&country=Norway&format=json&limit=1`);
  return response.data[0];
}

watch(postalCode, (newPostalCode) => {
  newPostalCode = newPostalCode.replace(/[^0-9]/g, '')

  if (newPostalCode.length === 4) {
    errorMessage.value = '';
  } else {
    errorMessage.value = 'Postkoden må bestå av fire sifre.';
  }
})

</script>

<template>
  <div id="divRegister" class="register-login-container">
    <h1>Registrer bruker</h1>
    <form v-on:submit.prevent>
      <div id="inputFields">
        <input type="text" placeholder="Fornavn" required>
        <input type="text" placeholder="Etternavn" required>
        <input type="text" placeholder="Brukernavn" required>
        <input type="email" placeholder="Epost" required>
        <input type="password" placeholder="Passord" required>
        <input type="password" placeholder="Gjenta passord" required>
        <input type="text" placeholder="Adresse" required>
        <input type="text" placeholder="Postkode" v-model="postalCode" required>
      </div>
      <p id="error">{{errorMessage}}</p>
      <br>
      <button class="good-button" type="submit"> Registrer </button>
    </form>
    <p class="register-login-text">
      Har du allerede en bruker? <a href="/login" class="link">Logg inn her</a>
    </p>
  </div>
</template>

<style scoped>

#inputFields {
  display: grid;
  grid-template-columns: repeat(1, auto);
  grid-template-rows: repeat(8, auto);
}

#error {
  font-size: small;
  color: var(--bad-red);
}

@media (min-width: 480px) {
  #inputFields {
    grid-template-columns: repeat(2, auto);
    grid-template-rows: repeat(4, auto);
    gap: 0px 20px;
  }

  .register-login-container {
    width: 500px;
  }
}

</style>