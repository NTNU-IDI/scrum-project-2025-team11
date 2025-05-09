<script setup lang="ts">
import {ref, watch, onMounted} from 'vue'
import axios from 'axios'
import {validateFirstName, validateLastName, validateEmail, validateUsername, validateHouseholdName, validatePassword} from "@/utils/validationService.ts";
import {registerNormalUser, login} from "@/api/AuthService.ts";
import { useReCaptcha } from 'vue-recaptcha-v3';

const { executeRecaptcha } = useReCaptcha() || {};

//Input-fields
const firstName = ref('')
const lastName = ref('')
const username = ref('')
const email = ref('')
const password = ref('')
const repeatedPassword = ref('')
const passwordInputType = ref('password')
const showPassword = ref(false)
//Input for existing household
const householdCode = ref('');
//Input for new household
const householdName = ref('')
const address = ref('')
const postalCode = ref('');
//Checkbox for privacyPolicy
const privacyPolicyCheck = ref(false)
//Handles logic related to what inputs are shown in the form
const householdChoice = ref("new")
const hasChosenNewHousehold = ref(true);
//References an error-message-<p>
const errorMessage = ref('');

const recaptchaToken = ref('');

function validateFields() {
  errorMessage.value = ""
  if (!validateFirstName(firstName.value)) {
    errorMessage.value = "Sørg for at fornavn kun består av bokstaver, mellomrom og eller bindestrek."
    return false
  }
  if (!validateLastName(lastName.value)) {
    errorMessage.value = "Sørg for at etternavn kun består av bokstaver, mellomrom og eller bindestrek."
    return false
  }
  if (!validateEmail(email.value)) {
    errorMessage.value = "Ugyldig format på email."
    return false
  }
  if (!validateUsername(username.value)) {
    errorMessage.value = "Sørg for at brukernavn kun består av bokstaver (eksklusivt æ, ø og å) og tall."
    return false
  }
  //Validates that the password matches the regex.
  if(!validatePassword(password.value) || !validatePassword(repeatedPassword.value)) {
    errorMessage.value = "Passordet ditt oppfyller ikke kravene om 8 tegn, med minst én stor bokstav, én liten bokstav," +
        " ett tall og ett spesialtegn."
    return false
  }
  //Validates that passwords are not empty and that they match
  if (!validateBothPasswords(password.value, repeatedPassword.value)) {
    errorMessage.value = "Passordene dine er tomme eller ulike."
    return false
  }
  if (householdChoice.value === "new") {
    if (!validateHouseholdName(householdName.value)) {
      errorMessage.value = "Sørg for at husholdningsnavn kun består av bokstaver og mellomrom."
      return false
    }
    if (address.value.toString() === "" || postalCode.value === "") {
      errorMessage.value = "Sørg for at addresseinformasjon og postkode er riktig."
      return false
    }
  }
  if (householdChoice.value === "existing") {
    if (householdCode.value.toString() === "") {
      errorMessage.value = "Sørg for å skrive inn husstandskode."
      return false
    }
  }
  if (!privacyPolicyCheck.value) {
    errorMessage.value = "Du må godta personvernerklæringen for å opprette bruker."
    return false
  }

  return true
}

async function attemptRegistration() {
  errorMessage.value = ""

  recaptchaToken.value = await executeRecaptcha('register');

  if (validateFields()) {
    console.log("All fields are valid")
    if(householdChoice.value === "new") {
      const location = await getLocationDataFromAdressAndPostalCode(address.value, postalCode.value)
      if (location === undefined) {
        errorMessage.value = "Addresse og/eller postkode er ugyldig. Vennligst forsikre deg om at disse er riktige og prøv igjen."
      } else {
        errorMessage.value = ""
        let latitude = location.lat
        let longitude = location.lon
        let city = location.address.city
        if (city === undefined) {
          city = location.address.municipality
        }
        await registerHouseholdAndCreateUser(latitude, longitude, city)
      }
    } else if(householdChoice.value === "existing") {
      await getHhIdFromInviteCodeAndRegister();
    }
  }
}

function validateBothPasswords(password: string, repeatedPassword: string) {
  if (password === "" || repeatedPassword === "") {
    return false
  }
  return password === repeatedPassword
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
          errorMessage.value = "Noe gikk galt under registreringen. Vennligst prøv igjen senere."
        }
      });
}

async function getHhIdFromInviteCodeAndRegister() {
  const inviteCode = householdCode.value;
  await axios.get("http://localhost:8080/api/household/inviteCode", {
    params: {
      inviteCode: inviteCode
    }
  })
      .then((response) => {
        createNewUser(response.data.id)
  })
      .catch((error) => {
        if (error.response.status === 400) {
          errorMessage.value = "Kunne ikke finne en husstand med den gitte koden."
        } else {
          errorMessage.value = "Noe uforventet oppsto. Vennligst vent og prøv igjen senere."
        }
      })
}

async function createNewUser(hhID: number) {
  registerNormalUser(firstName.value, lastName.value, username.value, email.value, password.value, hhID, recaptchaToken.value)
}

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

function changePasswordVisibility() {
  if(showPassword.value === false) {
    passwordInputType.value = 'password'
  } else {
    passwordInputType.value = 'text'
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

defineExpose({validateFields})

</script>

<template>
  <div id="divRegister" class="register-login-container">
    <h1>Registrer bruker</h1> <br>
    <form v-on:submit.prevent>
      <p>Vennligst legg inn informasjon for din egen bruker.</p>
      <div id="divConstantInputs">
        <label>Fornavn <span class="importantStar">*</span><input type="text"  v-model="firstName" id="iptFirstName" required> </label>
        <label>Etternavn <span class="importantStar">*</span><input type="text" v-model="lastName" id="iptLastName" required> </label>
        <label>Brukernavn <span class="importantStar">*</span><input type="text" placeholder="F.eks. OlaNord" v-model="username" id="iptUsername" required> </label>
        <label>Epost <span class="importantStar">*</span><input type="email" v-model="email" id="iptEmail" required> </label>
        <label>Passord <span class="importantStar">*</span><input :type="passwordInputType" v-model="password" id="iptPassword" required> </label>
        <label>Gjenta passord<span class="importantStar">*</span><input :type="passwordInputType" v-model="repeatedPassword" id="iptRepeatedPassword" required> </label>
      </div>
      <label><input type="checkbox" id="cbPassword" v-model="showPassword" @change="changePasswordVisibility"> Vis passord</label>
      <br> <br>
      <p>Vennligst legg inn informasjon om ny, eller eksisterende, husstand.</p>
      <label><input type="radio" v-model="householdChoice" value="new" @change="handleHouseholdChoice"> Ny husstand </label>
      <label><input type="radio" v-model="householdChoice" value="existing" @change="handleHouseholdChoice"> Eksisterende husstand </label>
      <br>
      <div id="divNewHouseholdInfo" v-if="hasChosenNewHousehold">
        <label>Husholdningsnavn <span class="importantStar">*</span><input type="text" placeholder="F.eks. 'Familien Nordmann'" v-model="householdName" id="iptHouseholdName" required> </label>
        <label>Adresse <span class="importantStar">*</span><input type="text" v-model="address" id="iptAddress" required> </label>
        <label>Postkode <span class="importantStar">*</span><input type="text" v-model="postalCode" id="iptPostalCode" required> </label>
      </div>
      <label v-if="!hasChosenNewHousehold"> Inviteringskode <span class="importantStar">*</span><input type="text" v-model="householdCode" id="iptHouseholdCode" required> </label><br><br>
      <label><input type="checkbox" v-model="privacyPolicyCheck" id="cbPrivacyPolicy" required>
        Jeg godtar og har lest <a href="/personvern" id="linkPrivacyPolicy" target="_blank" class="link"> personvernerklæringen</a> </label> <br>
      <button class="good-button" type="submit" @click="attemptRegistration"> Registrer </button>
      <p id="error" class="error-message">{{errorMessage}}</p>
    </form>
    <p class="register-login-text">
      Har du allerede en bruker? <router-link to="/login" class="link">Logg inn her</router-link>
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