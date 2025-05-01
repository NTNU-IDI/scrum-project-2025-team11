<script lang="ts" setup>
import { validateUsername, validateEmail, validatePassword } from '@/utils/validationService';
import { computed, ref } from 'vue';

const username = ref('');
const password = ref('');
const email = ref('');

const validInput = () => {
    if(!validateUsername(username.value)){
        alert('Invalid username');
        return false;
    }
    if(!validatePassword(password.value)){
        alert('Invalid password');
        return false;
    }
    if(!validateEmail(email.value)){
        alert('Invalid email');
        return false;
    }
    return true;
}

const createUser = () => {
    if(validInput()){
        // TODO: Call API to create user
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
                <input type="text" class="user-input" placeholder="*Brukernavn" v-model="username"></input>
                <input type="text" class="user-input" placeholder="*Passord" v-model="password"></input>
                <input type="text" class="user-input" placeholder="*E-mail" v-model="email"></input>
            </div>
            <button class="dark-button" @click="createUser" :disabled="areFieldsEmpty">+ Opprett adminbruker</button>
        </div>
    </div>
</div>
</template>
<style scoped>
.grey-container {
    background-color: var(--light-blue);
    height: 19rem;
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