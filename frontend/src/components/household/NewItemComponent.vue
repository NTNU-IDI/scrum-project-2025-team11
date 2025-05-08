<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { validateItemName, validateItemQuantity, validateItemUnit, validateItemExpirationDate } from '@/utils/validationService';
import { ItemService } from '@/api/ItemService';
import type { Item } from '@/types/Item';
import { useHouseholdStore } from '@/stores/householdStore';
import { useInventoryStore } from '@/stores/inventoryStore';
import { unitTypes } from '@/utils/values';
import { useToast } from 'vue-toast-notification';

/**
 * Store instances
 * @property {import('@/stores/householdStore').HouseholdStore}
 * @property {import('@/stores/inventoryStore').InventoryStore}
 */
const householdStore = useHouseholdStore();
const inventoryStore = useInventoryStore();

const $toast = useToast();

/**
 * Property to store the list of existing item types
 * @property {Array} existingTypes
 * @default []
 */
const existingTypes = ref<Item[]>([])

/**
 * Property to define if the type dropdown is shown
 * @property {boolean} isEditMode
 * @default false
 */
const showDropdown = ref(false);

/**
 * Property to define if the unit dropdown is shown
 * @property {boolean} isEditMode
 * @default false
 */
 const showUnitDropdown = ref(false);

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
const emit = defineEmits(['hide-new-item-box', 'new-item-success']);

// Fetch existing item types when the component is mounted
onMounted(async () => {
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

    try{
        await inventoryStore.upsertItem(newHouseholdItem);
        emit('new-item-success');
    } catch (error) {
        $toast.error('Feil ved opprettelse av artikkel!', {
            duration: 3000,
            position: 'top-right'
        });
    }
}
    

// Select type from dropdown
const selectOption = (itemName: string) => {
    newName.value = itemName;
    showDropdown.value = false;
}

// Add option to type dropdown
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

// Select unit from dropdown
const selectUnitOption = (itemName: string) => {
    newUnit.value = itemName;
    showUnitDropdown.value = false;
}

// Handle enter keydown event
const handleKeydown = (event: KeyboardEvent, name: string) => {
  if (event.key === 'Enter') {
    addItemOption(name);
    showUnitDropdown.value = false;
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
            <p>{{errorMsg}}</p>
            <div class="type-container"> 
                <label for="type-input" class="grey-text">Vare:</label>
                <input placeholder="f.eks. Hermetiske tomater" class="edit-input"
                    v-model="newName"
                    type="text"
                    id="type-input"
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
                <div class="quantity-label">
                    <label for="number-input" class="grey-text">Antall:</label>
                    <input type="number" class="edit-input" placeholder="f.eks. 10"
                        min="0" 
                        step="1" 
                        aria-label="Quantity" 
                        id="number-input"
                        v-model="newQuantity"
                    />
                </div>
                
                <div class="type-container">
                    <label for="unit-input" class="grey-text">Måleenhet:</label>
                    <input placeholder="f.eks. kg" class="edit-input"
                        v-model="newUnit"
                        type="text"
                        id="unit-input"
                        @focus="showUnitDropdown = true"
                        @keydown="handleKeydown($event, newUnit)"
                    />      
                    <ul v-if="showUnitDropdown" class="dropdown-list">
                        <li
                            v-for="type in unitTypes"
                            :key="type"
                            @click="selectUnitOption(type)"
                            class="dropdown-item"
                        >
                            {{ type }}
                        </li>
                    </ul>
                </div>
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

    #unit-input {
        width: 10rem;
    }

    .type-container {
        position: relative; 
    }
    
    .dropdown-list {
        position: absolute;
        top: 100%; /* Directly below the input */
        top: 3.2rem;
        left: 0;
        right: 0;
        max-height: 200px;
        width: 10rem;
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
        background-color: var(--good-green);
    }

    .cancel-button {
        position:absolute;
        top: -0.5rem;
        right: -9.5rem;
        color: var(--bad-red);
        background-color: transparent;
    }
</style>