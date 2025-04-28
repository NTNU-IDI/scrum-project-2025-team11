<script lang="ts" setup>
import { computed, ref } from 'vue';
import { validateItemName, validateItemQuantity, validateItemUnit, validateItemExpirationDate } from '@/utils/validationService';

const newName = ref('');
const newQuantity = ref('');
const newUnit = ref('');
const newExpirationDate = ref(new Date().toISOString().split('T')[0]);
const errorMsg = ref('');

const addItem = () => {
    if(!validateItemName(newName.value)) {
        errorMsg.value = 'Vennligst skriv inn et gyldig navn';
        console.log(errorMsg.value);
        alert(errorMsg.value);
        return;
    }
    if(!validateItemQuantity(newQuantity.value)) {
        errorMsg.value = 'Vennligst skriv inn en gyldig mengde';
        console.log(errorMsg.value);
        alert(errorMsg.value);
        return;
    }
    if(!validateItemUnit(newUnit.value)) {
        errorMsg.value = 'Vennligst skriv inn en gyldig enhet';
        console.log(errorMsg.value);
        alert(errorMsg.value);
        return;
    }
    if(!validateItemExpirationDate(newExpirationDate.value)) {
        errorMsg.value = 'Vennligst skriv inn en gyldig utløpsdato';
        console.log(errorMsg.value);
        alert(errorMsg.value);
        return;
    }

    // TODO: add item to list
    // TODO: hide component
}
const cancel = () => {
    // TODO: hide component
}

</script>
<template>
    <div class="grey-container">

        <div class="header-container">
            <h1 class="medium-header">Ny artikkel</h1>
            <button class="cancel-button" @click="cancel, $emit('hide-new-item-box')">X</button> 
        </div>

        <div class="item-input">
            <input type="text" class="edit-input" placeholder="Navn på vare" v-model="newName" />
            <div class="quantity-container">
                <input type="number" class="edit-input" placeholder="Mengde"
                    min="0" 
                    step="1" 
                    aria-label="Quantity" 
                    v-model="newQuantity"
                />
                <input type="text" class="edit-input" placeholder="Enhet" v-model="newUnit" />
            </div>
            <input type="date" class="edit-input" v-model="newExpirationDate" />
        </div>

        <div class="button-container">
            <button class="dark-button" @click="addItem, $emit('hide-new-item-box')">Legg til</button>
        </div>
    </div>
</template>
<style scoped>
    .grey-container {
        width: 20rem;
        height: 18rem;
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

    .item-input {
        padding: 0 1rem 0 0;
    }

    .quantity-container {
        display: flex;
        justify-content: space-between;
        gap: 1rem;
        margin: 0 -1rem 0 0;
    }

    .button-container {
        display: flex;
        justify-content: space-between;
    }

    .dark-button {
        width: 10rem; 
        height: 3rem; 
    }

    .cancel-button {
        position:absolute;
        top: -0.5rem;
        right: -9.5rem;
        color: var(--bad-red);
        background-color: transparent;
    }
</style>