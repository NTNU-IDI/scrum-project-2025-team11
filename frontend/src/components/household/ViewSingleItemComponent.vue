<script lang="ts" setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';
import { useHouseholdStore } from '@/stores/householdStore';
import type { HouseholdItemRequest, EditableItem } from '@/types/Inventory';
import { useInventoryStore } from '@/stores/inventoryStore';

/**
 * Store instances
 * @property {import('@/stores/householdStore').HouseholdStore}
 * @property {import('@/stores/inventoryStore').InventoryStore}
 * @property {import('@/stores/itemStore').ItemTypeStore}
 */
const householdStore = useHouseholdStore();
const itemTypeStore = useItemTypeStore();
const inventoryStore = useInventoryStore();

/**
 * Property to store the selected item type ID from store
 * @property {number | null} selectedTypeId 
 */
const itemTypeId = computed(() => itemTypeStore.id);

/**
 * Property to store the name of the selected item type
 * @property {string} itemTypeName
 */
const itemTypeName = computed(() => itemTypeStore.name);

/**
 * Property to store the list of items
 * @property {Array} items
 * @default []
 */
const items = ref<EditableItem[]>([]);

/**
 * Property to store all items of the selected type
 * @property {Array} allTypeItems
 * @default []
 */
const allTypeItems = ref<EditableItem[]>([]);

/**
 * Property to define if the component is in edit mode
 * @property {boolean} isEditMode
 * @default false
 */
const isEditMode = computed(() => itemTypeStore.isEditMode);

/**
 * Property to store the response message
 * @property {string} responseMessage
 * @default ''
 */
const responseMessage = ref('');

/**
 * Function to load the inventory items
 * @returns {Promise<void>}
 */
const loadItems = async () => {
    await householdStore.fetchHousehold();
    
    if (!householdStore.id) {
        console.error('Household ID is not available');
        return;
    }
    await inventoryStore.fetchInventory();
}

// Fetch inventory items when the component is mounted
onMounted( async () => {
    await loadItems();
});

// Watch for changes in the inventory and update the list
watch(() => inventoryStore.inventory, async (newItems) => {
    allTypeItems.value = newItems;
    if (itemTypeId.value) {
        filterItems();
    }
}, { immediate: true });

// Watch for changes in the selected item type
watch(itemTypeId, (newId) => {
    if (newId) {
        filterItems();
    }
});


// Watch for changes in the edit mode
watch(isEditMode, (newValue, oldValue) => {
    if (oldValue && !newValue) {
        updateItems();
        responseMessage.value = '';
    }
});

/**
 * Function to filter items based on selected item type
 * @returns {void}
 */
function filterItems() {
    items.value = allTypeItems.value
        .filter(item => item.itemId === itemTypeId.value)
        .map(item => ({
            householdId: item.householdId,
            itemId: item.itemId,
            itemName: item.itemName,
            quantity: item.quantity,
            unit: item.unit,
            expirationDate: item.expirationDate || '',
            acquiredDate: item.acquiredDate,
            dirty: false,
        }));
}

/**
 * Function to update an item
 * @returns {Promise<void>}
 */
const updateItems = async () => {
    for (const item of items.value) {
        if (item.dirty) {
            const updatedItem: HouseholdItemRequest = {
                itemId: item.itemId,
                quantity: item.quantity,
                unit: item.unit,
                expirationDate: item.expirationDate || '',
                acquiredDate: item.acquiredDate,
            };
            await inventoryStore.updateItem(item.householdId, updatedItem)
            responseMessage.value = `${item.itemName} er oppdatert`;
            item.dirty = false;
        }
    }
};

/**
 * Function to delete an item
 * @param {EditableItem} item - The item to delete
 * @returns {Promise<void>}
 */
const deleteItem = async (item: EditableItem) => {
    if (!householdStore.id) {
        console.error('Household ID is not available');
        responseMessage.value = 'Kunne ikke finne husstand';
        return;
    }
    if (!itemTypeId.value) {
        console.error('Item type ID is not available');
        responseMessage.value = 'Kunne ikke finne artikkeltype';
        return;
    }
    if(confirm('Er du sikker på at du vil slette denne artikkelen?')) {
        await inventoryStore.deleteItem(itemTypeId.value, item.acquiredDate)
        .then(() => {
            items.value = items.value.filter(item => item.itemId !== itemTypeId.value);
            responseMessage.value = `${item.itemName} er slettet fra lageret`;
        })
        .catch(error => {
            console.error('Error deleting item:', error);
        });
    }
}

</script>
<template>
    <h1 class="medium-header">Informasjon om artikkel</h1>

    <!-- Edit mode -->
    <div class="grey-container" v-if="isEditMode">
        <h2 class="small-header">{{ itemTypeName }}</h2>
        <div v-for="item in items" :key="item.expirationDate" class="item-card">
            <div class="delete-button" @click="deleteItem(item)">X</div>
            <div class="article-card">
                <input type="text" class="edit-input" id="quantity-input" @input="item.dirty = true" v-model="item.quantity" /> 
                <input type="text" class="edit-input" id="unit-input" @input="item.dirty = true" v-model="item.unit" />
                <div class="info">
                    <div class="exp-date">
                        <span class="grey-text">Utløper: </span> 
                        <input type="date" class="edit-input" id="date-input" @input="item.dirty = true" v-model="item.expirationDate" />
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- View mode -->
    <div class="grey-container" v-else>
        <h2 class="small-header">{{ itemTypeName }}</h2>
        <div v-for="item in items" :key="item.expirationDate" class="item-card">
            <div class="article-card">
                <p>{{ item.quantity }} {{ item.unit }}</p>
                <div class="info">
                    <div class="exp-date">
                        <span class="grey-text">Utløper: </span> {{ item.expirationDate }}
                    </div>
                </div>
            </div>
        </div>
    </div>

    <p class="user-response">{{ responseMessage }}</p>
</template>

<style scoped>
    .grey-container {
        background-color: var(--light-blue);
        margin-bottom: 4.1rem;
    }

    .small-header {
        color: white;
        font-size: var(--font-size-large);
        font-weight: normal;
        text-align: center;
    }

    .article-card {
        border: 2px solid white; 
        background-color: white;
    }

    .grey-text {
        color: grey;
        padding: 0.3rem;
    }

    .edit-input {
        background-color: transparent;
        border: none;
        color: var(--darkest-blue);
        font-size: var(--font-size-medium);
        width: 100%;
        max-width: 4rem;
        margin-top: 17px;
        padding: 0;
    }

    #quantity-input {
        max-width: 3rem;
    }

    #date-input {
        min-width: 7rem;
        font-family: 'Inter', sans-serif;     
    }

    .exp-date {
        display: flex;
        align-items: center;
    }

</style>