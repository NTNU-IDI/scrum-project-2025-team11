<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { validateItemName, validateItemQuantity, validateItemUnit, validateItemExpirationDate } from '@/utils/validationService';
import { ItemService } from '@/api/ItemService';
import type { Item } from '@/types/Item';
import { useHouseholdStore } from '@/stores/householdStore';
import { useInventoryStore } from '@/stores/inventoryStore';

/**
 * Store instances
 * @property {import('@/stores/householdStore').HouseholdStore}
 * @property {import('@/stores/inventoryStore').InventoryStore}
 */
const householdStore = useHouseholdStore();
const inventoryStore = useInventoryStore();

/**
 * Property to store the list of existing item types
 * @property {Array} existingTypes
 * @default []
 */
const existingTypes = ref<Item[]>([])

/**
 * Property to define if the dropdown is shown
 * @property {boolean} isEditMode
 * @default false
 */
const showDropdown = ref(false);

/**
 * Property to store the new item values
 * @property {string} newName
 * @property {string} newQuantity
 * @property {string} newUnit
 * @property {string} newExpirationDate
 */
const newName = ref('');
const newQuantity = ref('');
const newUnit = ref('');
const newExpirationDate = ref(new Date().toISOString().split('T')[0]);

/**
 * Property to store the response message
 * @property {string} responseMessage
 * @default ''
 */
const errorMsg = ref('');

/**
 * Property to define emits
 * @property {function} emit
 */
const emit = defineEmits(['hide-new-item-box', 'set-response-message']);

// Fetch existing item types when the component is mounted
onMounted(async () => {
    responseMessage.value = '';
    try {
        existingTypes.value = await ItemService.findAll();
    } catch (error) {
        console.error('Failed to load items:', error);
    }
});

/**
 * Function to add a new item to the inventory
 * @returns {Promise<void>}
 */
const addItem = async () => {
    if(!newName.value) {
        errorMsg.value = 'Vennligst velg en eksisterende vare eller opprett en ny';
        return;
    } 
    if(!newQuantity.value) {
        errorMsg.value = 'Mengde kan ikke være tom';
        return;
    }
    if(!validateItemQuantity(newQuantity.value)){
        errorMsg.value = 'Vennligst skriv inn en gyldig mengde';
        return;
    }
    
    if(!newUnit.value) {
        errorMsg.value = 'Enhet kan ikke være tom';
        return;
    }
    if(!validateItemUnit(newUnit.value)) {
        errorMsg.value = 'Vennligst skriv inn en gyldig enhet';
        return;
    }
    if(!newExpirationDate.value) {
        errorMsg.value = 'Utløpsdato kan ikke være tom';
        return;
    }
    if(!validateItemExpirationDate(newExpirationDate.value)) {
        errorMsg.value = 'Vennligst skriv inn en gyldig utløpsdato';
        return;
    }
    
    const newItemId = existingTypes.value.find(item => item.name === newName.value)?.id;
    if (!newItemId) {
        errorMsg.value = 'Vennligst velg en eksisterende vare eller opprett en ny';
        return;
    }
    const newHouseholdItem = {
        itemId: newItemId,
        name: newName.value,
        description: '',
        quantity: parseInt(newQuantity.value),
        unit: newUnit.value,
        acquiredDate: new Date().toISOString().slice(0, 19),
        expirationDate: newExpirationDate.value
    };
    if(!householdStore.id) {
        errorMsg.value = 'Kunne ikke finne husstand';
        return;
    }
    if(!newHouseholdItem) {
        errorMsg.value = 'Artikkel er ikke tilgjengelig';
        return;
    }
    if(!newHouseholdItem.itemId) {
        errorMsg.value = 'Artikkelen er ikke tilgjengelig';
        return;
    }

    await inventoryStore.upsertItem(householdStore.id, newHouseholdItem);
    errorMsg.value = `${newHouseholdItem.name} er lagt til i lageret`;
    emit('hide-new-item-box');
}

// Select type from dropdown
const selectOption = (itemName: string) => {
    newName.value = itemName;
    showDropdown.value = false;
}

// Add option to dropdown
const addItemOption = async (newName: string) => {
    if (!newName) {
        return;
    }
    if (newName && !existingTypes.value.some(item => item.name === newName)) {
        const newItem = await ItemService.create({ name: newName, description: '' });
        existingTypes.value.push(newItem);
        showDropdown.value = false;
    }
}

// Handle enter keydown event
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
            <button class="cancel-button" @click="() => {$emit('hide-new-item-box'); $emit('set-response-message', '');}">X</button> 
        </div>

        <div class="item-input">
            <p>{{errorMsg}}</p>
            <div class="type-container"> 
                <input placeholder="*Velg eksisterende eller ny vare..." class="edit-input"
                    v-model="newName"
                    type="text"
                    @focus="showDropdown = true"
                    @keydown="handleKeydown($event, newName)"
                />                           
                <ul v-if="showDropdown" class="dropdown-list">
                    <li
                        v-for="item in existingTypes"
                        :key="item.id"
                        @click="selectOption(item.name)"
                        class="dropdown-item"
                    >
                        {{ item.name }}
                    </li>
                </ul>
            </div>

            <div class="quantity-container">
                <input type="number" class="edit-input" placeholder="*Mengde"
                    min="0" 
                    step="1" 
                    aria-label="Quantity" 
                    v-model="newQuantity"
                />
                <input type="text" class="edit-input" placeholder="*Enhet" v-model="newUnit" />
            </div>
            <label for="date-input" class="grey-text">Utløpsdato:</label>
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
        height: auto;
    }

    .header-container {
        position: relative;
    }

    .medium-header {
        margin: 0.5rem 0 0 0;
    }

    p {
        margin: 0.5rem 0 0.5rem 0;
        color: var(--bad-red);
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
        background-color: var(--orange);
    }

    .cancel-button {
        position:absolute;
        top: -0.5rem;
        right: -9.5rem;
        color: var(--bad-red);
        background-color: transparent;
    }
</style>