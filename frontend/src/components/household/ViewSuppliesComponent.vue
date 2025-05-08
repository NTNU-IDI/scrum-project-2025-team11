<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';
import { useHouseholdStore } from '@/stores/householdStore';
import { ItemService } from '@/api/ItemService';
import { useInventoryStore } from '@/stores/inventoryStore';
import { useToast } from 'vue-toast-notification';

/**
 * Store instances
 * @property {import('@/stores/householdStore').HouseholdStore} 
 * @property {import('@/stores/inventoryStore').InventoryStore}
 * @property {import('@/stores/itemStore').ItemTypeStore}
 */
const householdStore = useHouseholdStore();
const inventoryStore = useInventoryStore();
const itemTypeStore = useItemTypeStore();

const $toast = useToast();

/**
* Property to store the selected item type ID
* @property {number | null} selectedTypeId 
*/
const selectedTypeId = ref<number | null>(null)

/**
 * Property to define if the component is in edit mode
 * @property {boolean} isEditMode
 * @default false
 */
const isEditMode = ref(false);

/**
 * Property to store the list of items
 * @property {Array} list
 * @default []
 */
const list = ref<{ id: number; name: string; quantity: number; unit: string; acquiredDate: string }[]>([]);

/**
 * Function to load the inventory items
 * @returns {Promise<void>}
 */
const loadInventory = async () => {
    await householdStore.fetchHousehold();

    if(!householdStore.id) {
        console.error('Household ID is not available');
        return [];
    }
    await inventoryStore.fetchInventory();
}

// Fetch inventory items when the component is mounted
onMounted( async () => {
    await loadInventory();
});

/**
 * Watch for changes in the inventory and update the list
 * @param {Array} newItems - The new items in the inventory
 * @returns {Promise<void>}
 */
watch(() => inventoryStore.inventory, async (newItems) => {
    const grouped: Record<number, { quantity: number; unit: string }> = {};
  
    for (const item of newItems) {
        if (grouped[item.itemId]) {
            grouped[item.itemId].quantity += item.quantity;
        } else {
            grouped[item.itemId] = {
                quantity: item.quantity,
                unit: item.unit
            };
        }
    }

    const uniqueItemIds = Object.keys(grouped).map(Number);
    const itemNames = await Promise.all(
        uniqueItemIds.map(id => ItemService.findById(id))
    );

    list.value = uniqueItemIds.map((id, index) => ({
        id,
        name: itemNames[index].name,
        quantity: grouped[id].quantity,
        unit: grouped[id].unit,
        acquiredDate: newItems.find(item => item.itemId === id)?.acquiredDate || ''
    }));
}, { immediate: true });

/**
 * Function to choose an item in the inventory
 * @param {number} itemTypeId - The ID of the item type
 * @param {string} name - The name of the item type
 */
const chooseItemType = (itemTypeId: any, name: string) => {
    selectedTypeId.value = itemTypeId;
    itemTypeStore.setItemType(itemTypeId, name);
}

// Toggle edit mode
const toggleEditMode = () => {
    itemTypeStore.toggleEditMode();
    isEditMode.value = !isEditMode.value;
}

/**
 * Function to delete an item from the inventory
 * @param {number} itemId - The ID of the item to delete
 * @returns {Promise<void>}
 */
const deleteItem =  async (itemId: number) => {
    const matchingItems = inventoryStore.inventory.filter(item => item.itemId === itemId);

    if (matchingItems.length === 0) return;

    const confirmed = confirm(`Er du sikker på at du vil slette alle ${matchingItems[0].itemName || ''}-enheter fra lageret?`);
    if (!confirmed) return;

    for (const item of matchingItems) {
        await inventoryStore.deleteItem(itemId, item.acquiredDate);
        if(!inventoryStore.inventory.map(item => item.itemId).includes(itemId)) {
            $toast.success('Vare er slettet fra lageret', {
            duration: 3000,
            position: 'top-right'
        });
        } else {
            $toast.error('Feil ved sletting av vare', {
            duration: 3000,
            position: 'top-right'
        });
        }
    }
    await loadInventory(); 
}
</script>
<template>
    <h1 class="medium-header">Beredskapslager</h1>

    <div class="grey-container">
        <div v-for="item in list" :key="item.id" class="item-card">
            <div v-if="isEditMode" class="delete-button" @click="deleteItem(item.id)">X</div>
            <div :class="['article-card', { active: item.id === selectedTypeId }]" @click="chooseItemType(item.id, item.name)">
                <div class="quantity">{{ item.quantity }} {{ item.unit }}</div>
                <div class="info">
                    <h2>{{ item.name }}</h2>
                </div>
            </div>
        </div>
    </div>

    <button :class="['dark-button', { active: isEditMode }]" @click="() => { toggleEditMode(); $emit('hide-new-item-box'); }">
        {{ isEditMode ? 'Ferdig' : 'Endre lager' }}
    </button>
</template>
<style scoped>
    .grey-container {
        width: 500px;
    }

    .quantity {
        display: flex;
        align-items: center;
    }

    .dark-button {
        height: 5rem;
        width: 9rem;
    }
    .dark-button.active {
        background-color: var(--good-green);
    }

    .article-card {
        cursor: pointer;
    }

    .article-card.active, .article-card:hover {
        background-color: var(--light-blue); 
        color: white;
    }

    .article-card.active .quantity, .article-card:hover .quantity, .article-card.active .edit-input, .article-card:hover .edit-input {
        color: white;
        background-color: transparent;
    }

    @media (max-width: 480px) {
        .grey-container {
            width: 100%;
        }
        .dark-button {
            width: 5;
            height: 3rem;
        }
    }
</style>