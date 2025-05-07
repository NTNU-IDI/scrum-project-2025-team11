<script lang="ts" setup>
import { ref } from 'vue';
import AddUserComponent from './manage_users/AddUserComponent.vue';
import ViewUsersComponent from './manage_users/ViewUsersComponent.vue';

const showNewUserBox = ref(false);

</script>
<template>
<div class="container">
    <div class="header-container"> 
        <h1>Administrere adminbrukere</h1>
        <p>Her kan du administrere adminbrukere. Husk at du igjennom dette grensesnittet har tilgang til krtiske opplysninger -  vennligst ta ansvaret som følger med det.</p>
    </div>   
    <div class="page-container">
        <ViewUsersComponent />

        <button class="dark-button" id="add-button" @click="showNewUserBox = true">+ Opprett ny adminbruker</button>
        <div class="modal-overlay" v-if="showNewUserBox" @click.self="showNewUserBox = false">
            <AddUserComponent
            @close="showNewUserBox = false"
            @hide-new-user-box="showNewUserBox = false"
            />
        </div>
    </div>
</div>
</template>
<style scoped>
.container{
    min-height: 80vh;
}
.header-container {
    margin: 2rem;
}

p {
    margin-top: 2rem;
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
</style>
