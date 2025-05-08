<script lang="ts" setup>
import { validateUsername, validateEmail, validatePassword, validateFirstName, validateLastName } from '@/utils/validationService';
import { computed, ref } from 'vue';
import { useAdminUserStore } from '@/stores/adminUserStore';
import { useHouseholdStore } from '@/stores/householdStore';
import {useToast} from 'vue-toast-notification';

// Store imports
const adminUserStore = useAdminUserStore();
const houseHoldStore = useHouseholdStore();

houseHoldStore.fetchHousehold(); 

const $toast = useToast();

// Props
const firstName = ref('');
const lastName = ref('');
const username = ref('');
const password = ref('');
const passwordRepeat = ref('');
const email = ref('');
const errorMsg = ref('');
const emit = defineEmits(['hide-new-user-box', 'new-user-success']);

const validInput = () => {
    if(!validateFirstName(firstName.value)){
        errorMsg.value = ('Invalid first name');
        return false;
    }
    if(!validateLastName(lastName.value)){
        errorMsg.value = ('Invalid last name');
        return false;
    }
    if(!validateUsername(username.value)){
        errorMsg.value = ('Invalid username');
        return false;
    }
    if(!validatePassword(password.value)){
        errorMsg.value = ('Invalid password');
        return false;
    }
    if(password.value !== passwordRepeat.value){
        errorMsg.value = ('Passwords do not match');
        return false;
    }
    if(!validateEmail(email.value)){
        errorMsg.value = ('Invalid email');
        return false;
    }
    return true;
}

const createUser = async () => {
    if (validInput()){
        if (houseHoldStore.id === null) {
            $toast.error('Husholdning eksisterer ikke', {
                duration: 3000,
                position: 'top-right'
            });
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
            errorMsg.value = '';
        }).catch((error) => {
            $toast.error('Feil ved opprettelse av bruker!' + error, {
                duration: 3000,
                position: 'top-right'
            });
        });
        if(adminUserStore.errorMsg !== '') {
            $toast.error(adminUserStore.errorMsg, {
                duration: 3000,
                position: 'top-right'
            });
        } else {
            emit('new-user-success');
        }
    }
}

const areFieldsEmpty = computed(() => {
    return firstName.value.trim() === '' ||  lastName.value.trim() === '' || username.value.trim() === '' || email.value.trim() === '' || password.value.trim() === '' || passwordRepeat.value.trim() === '';
});
</script>

<template>
<div class="page-container">
    <div class="header-box-container">
        <h1 class="medium-header">Legg til adminbruker</h1>
        <div class="grey-container">
            <div class="input-fields">
                <div class="header">Opprettelse av ny adminbruker</div>
                <button class="cancel-button" @click="$emit('hide-new-user-box')">X</button>
                <label class="label" for="first-name">*Fornavn</label>
                <input type="text" class="user-input" id="first-name" v-model="firstName"></input>
                <label class="label" for="last-name">*Etternavn</label>
                <input type="text" class="user-input" id="last-name" v-model="lastName"></input>
                <label class="label" for="username">*Brukernavn</label>
                <input type="text" class="user-input" id="username" v-model="username"></input>
                <label class="label" for="email">*E-mail</label>
                <input type="text" class="user-input" id="email" v-model="email"></input>
                <label class="label" for="password">*Passord</label>
                <input type="password" class="user-input" id="password" v-model="password"></input>
                <label class="label" for="repeat-pass">*Gjenta passord</label>
                <input type="password" class="user-input" id="repeat-pass" v-model="passwordRepeat"></input>
            </div>
            <button class="dark-button" @click="createUser" :disabled="areFieldsEmpty">+ Opprett adminbruker</button>
            <p class="error-message">{{ errorMsg }}</p>
        </div>
    </div>
</div>
</template>
<style scoped>
.grey-container {
    height: 42rem;
    padding-bottom: 0;
}

.header {
    color: var(--dark-blue);
    text-align: left;
    font-size: var(--font-size-large);
    margin-bottom: 0.5rem;
    background-color: transparent;
}

h1 {
    text-align: left;
}

.error-message {
    font-size: var(--font-size-medium);
    color: var(--bad-red);
}

.user-input {
    height: 3rem;
    background-color: white;
}

.dark-button {
    background-color: var(--good-green);
    height: 3rem;
    width: 15rem;
    margin: 0;
}

.dark-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.cancel-button {
    position: absolute;
    top: 6.5rem;
    right: -10.5rem;
    color: var(--bad-red);
    background-color: transparent;
}
</style>