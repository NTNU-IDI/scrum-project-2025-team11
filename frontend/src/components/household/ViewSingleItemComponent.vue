<script lang="ts" setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';
import { formatDate, formatDateToList } from '@/utils/formatDate';
import { InventoryService } from '@/api/InventoryService';
import { useHouseholdStore } from '@/stores/householdStore';
import { HouseholdService } from '@/api/HouseholdService';
import type { HouseholdItemRequest, EditableItem } from '@/types/Inventory';
import { useInventoryStore } from '@/stores/inventoryStore';
import { ItemService } from '@/api/ItemService';

// Store imports
const householdStore = useHouseholdStore();
const itemTypeStore = useItemTypeStore();
const inventoryStore = useInventoryStore();

// Props
const itemTypeId = computed(() => itemTypeStore.id);
const itemTypeName = computed(() => itemTypeStore.name);
const items = ref<EditableItem[]>([]);
const allTypeItems = ref<EditableItem[]>([]);
const isEditMode = computed(() => itemTypeStore.isEditMode);

// Load all items of the selected type
const loadItems = async () => {
    // Get actual household id from the store
    await householdStore.fetchHousehold();
    
    if (!householdStore.id) {
        console.error('Household ID is not available');
        return;
    }
    await inventoryStore.fetchInventory(householdStore.id);
}

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

watch(itemTypeId, (newId) => {
    if (newId) {
        filterItems();
    }
});

watch(isEditMode, (newValue, oldValue) => {
    if (oldValue && !newValue) {
        updateItems();
    }
});

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
            item.dirty = false;
        }
    }
};

const deleteItem = async (item: EditableItem) => {
    if (!householdStore.id) {
        console.error('Household ID is not available');
        return;
    }
    if (!itemTypeId.value) {
        console.error('Item type ID is not available');
        return;
    }
    if(confirm('Er du sikker på at du vil slette denne artikkelen?')) {
        await inventoryStore.deleteItem(householdStore.id, itemTypeId.value, item.acquiredDate)
        .then(() => {
            items.value = items.value.filter(item => item.itemId !== itemTypeId.value);
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