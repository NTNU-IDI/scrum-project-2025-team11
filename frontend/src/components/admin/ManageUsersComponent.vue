<script lang="ts" setup>
import { ref } from 'vue';
import AddUserComponent from './manage_users/AddUserComponent.vue';
import ViewUsersComponent from './manage_users/ViewUsersComponent.vue';
import {useToast} from 'vue-toast-notification';

const showNewUserBox = ref(false);
const $toast = useToast();

const displaySuccess = () => {
    showNewUserBox.value = false
    $toast.success('Bruker opprettet!', {
        duration: 3000,
        position: 'top-right'
    });
};

</script>
<template>
<div class="container">
    <div class="header-box"> 
        <h1>Administrere adminbrukere</h1>
        <p>Her kan du administrere adminbrukere. Husk at du igjennom dette grensesnittet har tilgang til kritiske opplysninger -  vennligst ta ansvaret som følger med det.</p>
    </div>   
    <div class="page-container">
        <ViewUsersComponent />

        <button class="dark-button" id="add-button" @click="showNewUserBox = true">+ Opprett ny adminbruker</button>
        <div class="modal-overlay" v-if="showNewUserBox" @click.self="showNewUserBox = false">
            <AddUserComponent
            @close="showNewUserBox = false"
            @hide-new-user-box="showNewUserBox = false"
            @new-user-success="displaySuccess"
            />
        </div>
    </div>
</div>
</template>
<style scoped>
.container{
    min-height: 80vh;
}
.header-box {
    margin: 2rem;
}

h1 {
    text-align: left;
}

p {
    margin-top: 2rem;
    text-align: left;
}

h1 {
    text-align: left;
}

.page-container {
    display: flex;
    flex-direction: row;
    margin: 1rem;
}

.dark-button {
    height: 3rem;
    width: 14rem;
    background-color: var(--orange);
    margin-left: auto;
    margin-top: 4.7rem;
}

@media (max-width: 480px) {
    .page-container {
        flex-direction: column;
    }
   
    .modal-overlay {
        overflow-x: hidden;
        top: 0;
    }
}
</style>
