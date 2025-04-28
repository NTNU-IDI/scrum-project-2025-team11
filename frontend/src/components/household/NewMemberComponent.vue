<script lang="ts" setup>
import { computed, ref } from 'vue';
import { validateFirstName, validateLastName } from '@/utils/validationService';

const newFirstName = ref('');
const newLastName = ref('');
const errorMsg = ref('');

const emit = defineEmits(['hide-new-member-box']);

const addMember = () => {
    if(!validateFirstName(newFirstName.value)) {
        errorMsg.value = 'Vennligst skriv inn et gyldig fornavn';
        console.log(errorMsg.value);
        alert(errorMsg.value);
        return;
    }
    if(!validateLastName(newLastName.value)) {
        errorMsg.value = 'Vennligst skriv inn et gyldig etternavn';
        console.log(errorMsg.value);
        alert(errorMsg.value);
        return;
    }

    emit('hide-new-member-box');
    // TODO: add item to list
    // TODO: hide component
}

const cancel = () => {
    // TODO: hide component
}

const sendInvitationLink = () => {
    // TODO: send invitation link
}

</script>
<template>
    <div class="grey-container">
        <div class="header-container">
            <h1 class="medium-header">Nytt medlem</h1>
            <button class="cancel-button" @click="() => { cancel(); $emit('hide-new-member-box'); }">X</button> 
        </div>

        <div class="member-input">
            <input type="text" class="edit-input" placeholder="Fornavn" v-model="newFirstName" />
            <input type="text" class="edit-input" placeholder="Etternavn" v-model="newLastName" />
        </div>

        <div class="button-container">
            <button class="dark-button" @click="addMember">Legg til</button>
        </div>

        <p>Eller</p>

        <div class="button-container">
            <button class="dark-button" id="invite-button" @click="sendInvitationLink">Send invitasjonslink</button>
        </div>
    </div>
</template>
<style scoped>
    .grey-container {
        width: 20rem;
        height: 22rem;
    }

    .header-container {
        position: relative;
    }

    .medium-header {
        margin: 0.5rem 0 1rem 0;
    }

    .edit-input {
        background-color: white; 
    }

    .member-input {
        padding: 0 1rem 0 0;
    }

    .button-container {
        display: flex;
        justify-content: space-between;
    }

    .dark-button {
        width: 10rem; 
        height: 3rem; 
    }

    #invite-button {
        background-color: var(--light-blue);
        width: auto;
    }

    .cancel-button {
        position:absolute;
        top: -0.5rem;
        right: -9.5rem;
        color: var(--bad-red);
        background-color: transparent;
    }
</style>