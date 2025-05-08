<script lang="ts" setup>
import { ref } from 'vue';
import { validateEmail } from '@/utils/validationService';    
import { useToast } from 'vue-toast-notification';
import { useHouseholdStore } from '@/stores/householdStore';

const $toast = useToast();
const householdStore = useHouseholdStore();
const email = ref('');
const errorMsg = ref('');

const emit = defineEmits(['hide-new-member-box', 'invite-success']);

const sendInvitationLink = async () => {
    if(!validateEmail(email.value)) {
        errorMsg.value = 'Vennligst skriv inn en gyldig e-postadresse';
        return;
    }
    try{
        await householdStore.inviteMember(email.value.trimEnd())
        emit('invite-success');
        email.value = '';
        errorMsg.value = '';
    } catch (error) {
        $toast.error(householdStore.errrMsg || 'Noe gikk galt. Vennligst prøv igjen.', {
            duration: 3000,
            position: 'top-right'
        });
    }
}

</script>
<template>
    <div class="grey-container">
        <p v-if="errorMsg" class="error-message">{{ errorMsg }}</p>
        <div class="header-container">
            <h1 class="medium-header">Inviter nytt medlem</h1>
            <button class="cancel-button" @click="$emit('hide-new-member-box')">X</button> 
        </div>

        <div class="member-input">
            <input type="text" class="edit-input" placeholder="E-mail" v-model="email" />
        </div>

        <div class="button-container">
            <button class="dark-button" id="invite-button" @click="sendInvitationLink">Send invitasjonslink</button>
        </div>
    </div>
</template>
<style scoped>
    .grey-container {
        width: 20rem;
        height: auto;
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
        background-color: var(--orange);
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