
<script setup lang="ts">
import {ref, watch, onMounted} from 'vue'
import axios from 'axios'

const postalCode = ref('');
const householdCode = ref('');
const householdChoice = ref()
const hasChosenNewHousehold = ref();
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
  //Got help from ChatGPT with the use of 'street', 'postalcode' and 'country'
  const response = await axios.get(`https://nominatim.openstreetmap.org/search?street=${address}&postalcode=${postalCode}&country=Norway&format=json&limit=1`);
  return response.data[0];
}

function handleHouseholdChoice() {
  if (householdChoice.value === "new") {
    hasChosenNewHousehold.value = true;
  } else if (householdChoice.value === "existing"){
    hasChosenNewHousehold.value = false;
  }
}

watch(postalCode, (newPostalCode) => {
  newPostalCode = newPostalCode.replace(/[^0-9]/g, '')

  if (newPostalCode.length === 4) {
    //errorMessage.value = '';
  } else {
    //errorMessage.value = 'Postkoden må bestå av fire sifre.';
  }
})

onMounted(() => {
  householdChoice.value = "new"
  handleHouseholdChoice()
})

</script>

<template>
    <div class="register-login-container">
        <h1>Logg inn</h1>
        <input type="text" placeholder="Brukernavn"/>
        <input type="password" placeholder="Passord"/>
        <button class="good-button">Logg inn</button>
        <p class="register-login-text">
        Har du ikke en bruker?
        <router-link to="/register" class="link">Registrer deg her</router-link>
        </p>
    </div>

  <div id="divRegister" class="register-login-container">
    <h1>Registrer bruker</h1>
    <form v-on:submit.prevent>
      <div id="divConstantInputs">
        <input type="text" placeholder="Fornavn" required>
        <input type="text" placeholder="Etternavn" required>
        <input type="text" placeholder="Brukernavn" required>
        <input type="email" placeholder="Epost" required>
        <input type="password" placeholder="Passord" required>
        <input type="password" placeholder="Gjenta passord" required>
      </div>
      <label><input type="radio" v-model="householdChoice" value="new" @change="handleHouseholdChoice"> Ny husstand</label> <br>
      <div id="divPlaceInfo" v-if="hasChosenNewHousehold">
        <input type="text" placeholder="Adresse" required>
        <input type="text" placeholder="Postkode" v-model="postalCode" required>
      </div>
      <label><input type="radio" v-model="householdChoice" value="existing" @change="handleHouseholdChoice"> Eksisterende husstand </label>
      <input type="text" id="iptHouseholdCode" v-if="!hasChosenNewHousehold" placeholder="Husstandsskode" required><br><br>
      <label><input type="checkbox" required>
        Jeg godtar og har lest <a href="/personvern" target="_blank" class="link"> personvernerklæringen</a> </label> <br>
      <button class="good-button" type="submit"> Registrer </button>
      <p id="error">{{errorMessage}}</p>
    </form>
    <p class="register-login-text">
      Har du allerede en bruker? <a href="/login" class="link">Logg inn her</a>
    </p>
  </div>
</template>

<style scoped>

#divConstantInputs {
  display: grid;
  grid-template-columns: repeat(1, auto);
  grid-template-rows: repeat(8, auto);
}

input[type="radio"], input[type="checkbox"]{
  width: auto;
}

label {
  width: auto;
}

#error {
  font-size: var(--font-size-small);
  color: var(--bad-red);
}

@media (min-width: 480px) {
  #divConstantInputs {
    grid-template-columns: repeat(2, auto);
    grid-template-rows: repeat(3, auto);
    gap: 0px 20px;
  }

  .register-login-container {
    width: 500px;
  }
}

</style>

