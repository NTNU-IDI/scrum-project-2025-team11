<script setup lang="ts">
import {ref, watch, onMounted} from 'vue'
import axios from 'axios'
import {useRouter} from "vue-router";
import {validateFirstName, validateLastName, validateEmail, validateUsername, validateHouseholdName} from "@/utils/validationService.ts";

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
const privacyPolicyCheck = ref(false)
//Handles logic related to what inputs are shown in the form
const householdChoice = ref("new")
const hasChosenNewHousehold = ref(true);
//References an error-message-<p>
const errorMessage = ref('');

function validateFields() {
  let errorInfo = []
  errorMessage.value = ""
  if (!validateFirstName(firstName.value)) {
    errorInfo.push("hei")
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
    errorMessage.value = "Sørg for at brukernavn kun består av bokstaver og tall."
    return false
  }
  if (!validatePassword(password.value, repeatedPassword.value)) {
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
  if (validateFields()) {
    if(householdChoice.value === "new") {
      const location = await getLocationDataFromAdressAndPostalCode(address.value, postalCode.value)
      if (location === undefined) {
        errorMessage.value = "Addresse og/eller postkode er ugyldig. Vennligst forsikre deg om at disse er riktige og prøv igjen."
      } else {
        errorMessage.value = ""
        await registerHouseholdAndCreateUser(location.lat, location.lon, location.address.city)
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

function validatePassword(password: string, repeatedPassword: string) {
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
      .then((response) => {
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

defineExpose({validateFields})

</script>

<template>
  <div id="divRegister" class="register-login-container">
    <h1>Registrer bruker</h1>
    <form v-on:submit.prevent>
      <div id="divConstantInputs">
        <input type="text" placeholder="Fornavn" v-model="firstName" id="iptFirstName"required>
        <input type="text" placeholder="Etternavn" v-model="lastName" id="iptLastName" required>
        <input type="text" placeholder="Brukernavn" v-model="username" id="iptUsername" required>
        <input type="email" placeholder="Epost" v-model="email" id="iptEmail" required>
        <input type="password" placeholder="Passord" v-model="password" id="iptPassword" required>
        <input type="password" placeholder="Gjenta passord" v-model="repeatedPassword" id="iptRepeatedPassword" required>
      </div>
      <label><input type="radio" v-model="householdChoice" value="new" @change="handleHouseholdChoice"> Ny husstand</label> <br>
      <div id="divNewHouseholdInfo" v-if="hasChosenNewHousehold">
        <input type="text" placeholder="Husholdningsnavn, f.eks. 'Familien Madsen'" v-model="householdName" id="iptHouseholdName" required>
        <input type="text" placeholder="Adresse" v-model="address" id="iptAddress" required>
        <input type="text" placeholder="Postkode" v-model="postalCode" id="iptPostalCode" required>
      </div>
      <label><input type="radio" v-model="householdChoice" value="existing" @change="handleHouseholdChoice"> Eksisterende husstand </label>
      <input type="text" v-if="!hasChosenNewHousehold" placeholder="Husstandsskode" v-model="householdCode" id="iptHouseholdCode" required><br><br>
      <label><input type="checkbox" v-model="privacyPolicyCheck" id="cbPrivacyPolicy" required>
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