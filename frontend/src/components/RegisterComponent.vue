<script setup lang="ts">
import {ref, watch, onMounted} from 'vue'
import axios from 'axios'
import {useRouter} from "vue-router";

const router = useRouter()

const REGISTRATION_FAILURE_WAIT_MESSAGE = "Noe gikk galt under registreringen. Vennligst prøv igjen senere."

//Input-fields
const firstName = ref('')
const lastName = ref('')
const username = ref('')
const email = ref('')
const password = ref('')
const repeatedPassword = ref('')
//Input for existing household
const householdCode = ref('');
//Input for new household
const householdName = ref('')
const address = ref('')
const postalCode = ref('');
//Checkbox for privacyPolicy
const privacyPolicyCheck = ref('')
//Handles logic related to what inputs are shown in the form
const householdChoice = ref()
const hasChosenNewHousehold = ref();
//References an error-message-<p>
const errorMessage = ref('');

async function attemptRegistration() {
  errorMessage.value = ""

  if (isValidPassword()) {
    if(hasChosenNewHousehold) {
      const location = await getLocationDataFromAdressAndPostalCode(address.value, postalCode.value)
      if (location === undefined) {
        errorMessage.value = "Addresse og/eller postkode er ugyldig. Vennligst forsikre deg om at disse er riktige og prøv igjen."
      } else {
        errorMessage.value = ""
        registerHouseholdAndCreateUser(location.lat, location.lon, location.address.city)
      }
    } else {
      //TODO: Implement how creation of new user joining existing household works when backend is ready
      //Will probably have to firstly use the code in the input-field to retrieve the hhid from backend, and then
      //use the hhid when creating the new user.
      //getHousholdAndCreateUser()
      //createNewUser(householdCode.value)
    }
  }
}

function isValidPassword() {
  if (password.value.toString() !== repeatedPassword.value.toString()) {
    errorMessage.value = "Du har ikke skrevet like passord. Vennligst sjekk disse og prøv igjen."
    return false
  }
  return true
}

async function registerHouseholdAndCreateUser(latitude: number, longitude: number, city: string) {
  await axios.post("http://localhost:8080/api/household", {
    name: householdName.value,
    memberCount: 1,
    address: {
      street: address.value,
      postalCode: postalCode.value,
      city: city,
      latitude: latitude,
      longitude: longitude
    }
  })
      .then((response) => {
        createNewUser(response.data.id)
      })
      .catch((error) => {
        if (error.response.status === 400) {
          errorMessage.value = "Ugyldig husstands-informasjon. Vennligst kontroller og prøv igjen."
        } else {
          errorMessage.value = REGISTRATION_FAILURE_WAIT_MESSAGE
        }
      });
}

async function createNewUser(hhID: number) {
  await axios.post("http://localhost:8080/api/users", {
    email: email.value,
    username: username.value,
    firstName: firstName.value,
    lastName: lastName.value,
    password: password.value,
    householdId: hhID
  })
      .then(() => {
        router.push("/")
      })
      .catch((error) => {
        if (error.response.status === 409) {
          errorMessage.value = "Brukernavn eller email er allerede tatt."
        } else if (error.response.status === 500) {
          errorMessage.value = "Backend ga 500"
        } else {
          errorMessage.value = REGISTRATION_FAILURE_WAIT_MESSAGE
        }
      })
}

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
  const response = await axios.get(`https://nominatim.openstreetmap.org/search?street=${address}&postalcode=${postalCode}&country=Norway&format=json&limit=1&addressdetails=1`);
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
  postalCode.value = newPostalCode.replace(/[^0-9]/g, '')

  if (postalCode.value.length === 4) {
    errorMessage.value = '';
  } else {
    errorMessage.value = 'Postkoden må bestå av fire sifre.';
  }
})

onMounted(() => {
  householdChoice.value = "new"
  handleHouseholdChoice()
})

</script>

<template>
  <div id="divRegister" class="register-login-container">
    <h1>Registrer bruker</h1>
    <form v-on:submit.prevent>
      <div id="divConstantInputs">
        <input type="text" placeholder="Fornavn" v-model="firstName" required>
        <input type="text" placeholder="Etternavn" v-model="lastName" required>
        <input type="text" placeholder="Brukernavn" v-model="username" required>
        <input type="email" placeholder="Epost" v-model="email" required>
        <input type="password" placeholder="Passord" v-model="password" required>
        <input type="password" placeholder="Gjenta passord" v-model="repeatedPassword" required>
      </div>
      <label><input type="radio" v-model="householdChoice" value="new" @change="handleHouseholdChoice"> Ny husstand</label> <br>
      <div id="divNewHouseholdInfo" v-if="hasChosenNewHousehold">
        <input type="text" placeholder="Husholdningsnavn, f.eks. 'Familien Madsen'" v-model="householdName" required>
        <input type="text" placeholder="Adresse" v-model="address" required>
        <input type="text" placeholder="Postkode" v-model="postalCode" required>
      </div>
      <label><input type="radio" v-model="householdChoice" value="existing" @change="handleHouseholdChoice"> Eksisterende husstand </label>
      <input type="text" id="iptHouseholdCode" v-if="!hasChosenNewHousehold" placeholder="Husstandsskode" v-model="householdCode" required><br><br>
      <label><input type="checkbox" id="cbPrivacyPolicy" v-model="privacyPolicyCheck" required>
        Jeg godtar og har lest <a href="/personvern" id="linkPrivacyPolicy" target="_blank" class="link"> personvernerklæringen</a> </label> <br>
      <button class="good-button" type="submit" @click="attemptRegistration"> Registrer </button>
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