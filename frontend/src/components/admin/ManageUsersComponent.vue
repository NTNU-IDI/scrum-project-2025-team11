<script lang="ts" setup>
import AddUserComponent from './manage_users/AddUserComponent.vue';
import ViewUsersComponent from './manage_users/ViewUsersComponent.vue';


</script>
<template>
<div class="header-container"> 
    <h1>Administrere adminbrukere</h1>
    <p>Dette er siden for administrering av adminbrukere. Husk at du igjennom dette grensesnittet har tilgang til krtiske opplysninger -  vennligst ta ansvaret som følger med det.</p>
</div>   
<div class="page-container">
    <ViewUsersComponent />
    <AddUserComponent />
</div>
</template>
<style scoped>
.header-container {
    margin-left: 2rem;
}

.page-container {
    display: flex;
    flex-direction: row;
    margin: 1rem;
}
</style>
