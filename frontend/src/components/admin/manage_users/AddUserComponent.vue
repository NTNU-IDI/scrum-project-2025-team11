<script lang="ts" setup>
import { validateUsername, validateEmail, validatePassword, validateFirstName, validateLastName } from '@/utils/validationService';
import { computed, ref } from 'vue';
import { useAdminUserStore } from '@/stores/adminUserStore';
import { useHouseholdStore } from '@/stores/householdStore';

// Store imports
const adminUserStore = useAdminUserStore();
const houseHoldStore = useHouseholdStore();
// Remove 
houseHoldStore.setHousehold(1); 

// Props
const firstName = ref('');
const lastName = ref('');
const username = ref('');
const password = ref('');
const passwordRepeat = ref('');
const email = ref('');

const validInput = () => {
    if(!validateFirstName(firstName.value)){
        alert('Invalid first name');
        return false;
    }
    if(!validateLastName(lastName.value)){
        alert('Invalid last name');
        return false;
    }
    if(!validateUsername(username.value)){
        alert('Invalid username');
        return false;
    }
    if(!validatePassword(password.value)){
        alert('Invalid password');
        return false;
    }
    if(password.value !== passwordRepeat.value){
        alert('Passwords do not match');
        return false;
    }
    if(!validateEmail(email.value)){
        alert('Invalid email');
        return false;
    }
    return true;
}

const createUser = async () => {
    if (validInput()){
        if (houseHoldStore.id === null) {
            alert('Household ID is not set.');
            return;
        }   
        await adminUserStore.createUser({
            firstName: firstName.value,
            lastName: lastName.value,
            username: username.value,
            password: password.value,
            email: email.value,
            householdId: houseHoldStore.id
        }).then(() => {
            firstName.value = '';
            lastName.value = '';
            username.value = '';
            email.value = '';
            password.value = '';
            passwordRepeat.value = '';
        }).catch((error) => {
            alert('Error creating user: ' + error.message);
        });
    }
}

const areFieldsEmpty = computed(() => {
    return username.value.trim() === '' || password.value.trim() === '' || email.value.trim() === '';
});
</script>

<template>
<div class="page-container">
    <div class="header-box-container">
        <h1 class="medium-header">Legg til adminbruker</h1>
        <div class="grey-container">
            <div class="input-fields">
                <div class="header">Opprettelse av ny adminbruker</div>
                <input type="text" class="user-input" placeholder="*Fornavn" v-model="firstName"></input>
                <input type="text" class="user-input" placeholder="*Etternavn" v-model="lastName"></input>
                <input type="text" class="user-input" placeholder="*Brukernavn" v-model="username"></input>
                <input type="text" class="user-input" placeholder="*E-mail" v-model="email"></input>
                <input type="password" class="user-input" placeholder="*Passord" v-model="password"></input>
                <input type="password" class="user-input" placeholder="*Gjenta passord" v-model="passwordRepeat"></input>
            </div>
            <button class="dark-button" @click="createUser" :disabled="areFieldsEmpty">+ Opprett adminbruker</button>
        </div>
    </div>
</div>
</template>
<style scoped>
.grey-container {
    background-color: var(--light-blue);
    height: 31rem;
}

.header {
    color: white;
    font-weight: bold;
    margin-bottom: 1.5rem;
}

.user-input {
    height: 3rem;
}

.dark-button {
    background-color: var(--orange);
    height: 3rem;
    width: 15rem;
}

.dark-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}
</style>