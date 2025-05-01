<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';
import { InventoryService } from '@/api/InventoryService';
import { useHouseholdStore } from '@/stores/householdStore';
import { ItemService } from '@/api/ItemService';
import { useInventoryStore } from '@/stores/inventoryStore';

// Store imports
const householdStore = useHouseholdStore();
const inventoryStore = useInventoryStore();
const itemTypeStore = useItemTypeStore();

// Props
const selectedTypeId = ref<number | null>(null)
const isEditMode = ref(false);
const list = ref<{ id: number; name: string; quantity: number; unit: string; acquiredDate: string }[]>([]);

// Fetch inventory items
const loadInventory = async () => {
    // TODO: Get actual household id from the store
    await householdStore.setHousehold(1); 

    if(!householdStore.id) {
        console.error('Household ID is not available');
        return [];
    }
    await inventoryStore.fetchInventory(householdStore.id);
}

// Fetch inventory items when the component is mounted
onMounted( async () => {
    await loadInventory();
});


// Watch for changes in the inventory and update the list
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

// Choose an item in the supply
const chooseItemType = (itemTypeId: any, name: string) => {
    selectedTypeId.value = itemTypeId;
    itemTypeStore.setItemType(itemTypeId, name);
}

// Toggle edit mode
const toggleEditMode = () => {
    itemTypeStore.toggleEditMode();
    isEditMode.value = !isEditMode.value;
}

// Delete an item from the inventory
const deleteItem =  (itemId: number) => {
    list.value.forEach(async item => {
        if (!householdStore.id) {
            console.error('Household ID is not available');
            return;
        }
        if (item.id === itemId) {
            await inventoryStore.deleteItem(householdStore.id, itemId, item.acquiredDate);
        }
    });
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
        {{ isEditMode ? 'Large' : 'Endre lager' }}
    </button>
</template>
<style scoped>
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
</style>