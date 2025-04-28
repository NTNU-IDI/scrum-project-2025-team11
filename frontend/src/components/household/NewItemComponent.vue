<script lang="ts" setup>
import { computed, ref } from 'vue';
import { validateItemName, validateItemQuantity, validateItemUnit, validateItemExpirationDate } from '@/utils/validationService';

//TODO: fetch existing item names from API
//const existingItems = computed(() => {});

// Remove later
const existingItems = ref(['Hermetiske tomater', 'Vann', 'Mel']);

const showDropdown = ref(false);
const newName = ref('');
const newQuantity = ref('');
const newUnit = ref('');
const newExpirationDate = ref(new Date().toISOString().split('T')[0]);
const errorMsg = ref('');

const emit = defineEmits(['hide-new-item-box']);

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

    emit('hide-new-item-box');
    // TODO: add item to list
}

const selectOption = (itemName: string) => {
    newName.value = itemName;
    showDropdown.value = false;
}
const addItemOption = (newName: string) => {
    if (newName && !existingItems.value.includes(newName)) {
        existingItems.value.push(newName);
    }
    showDropdown.value = false;
    emit('hide-new-item-box');
}

const handleKeydown = (event: KeyboardEvent, name: string) => {
  if (event.key === 'Enter') {
    addItemOption(name);
  }
};

</script>
<template>
    <div class="grey-container">

        <div class="header-container">
            <h1 class="medium-header">Ny artikkel</h1>
            <button class="cancel-button" @click="$emit('hide-new-item-box')">X</button> 
        </div>

        <div class="item-input">
            <div class="type-container"> 
                <input placeholder="Velg eksisterende eller ny vare..." class="edit-input"
                    v-model="newName"
                    type="text"
                    @focus="showDropdown = true"
                    @keydown="handleKeydown($event, newName)"
                />                           
                <ul v-if="showDropdown" class="dropdown-list">
                    <li
                        v-for="item in existingItems"
                        :key="item"
                        @click="selectOption(item)"
                        class="dropdown-item"
                    >
                        {{ item }}
                    </li>
                </ul>
            </div>

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
            <button class="dark-button" @click="addItem">Legg til</button>
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
        margin-right: 1rem;
    }

    .type-container {
        position: relative; 
    }
    
    .dropdown-list {
        position: absolute;
        top: 100%; /* Directly below the input */
        top: 2rem;
        left: 0;
        right: 0;
        max-height: 200px;
        overflow-y: auto;
        background: white;
        border-radius: 0.25rem;
        margin-top: 0.25rem;
        z-index: 1000;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
        padding: 0;
        list-style: none;
    }
    
    .dropdown-item {
        padding: 0.5rem 1rem;
        cursor: pointer;
        transition: background-color 0.2s ease;
    }
    
    .dropdown-item:hover {
        background-color: #f0f0f0;
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