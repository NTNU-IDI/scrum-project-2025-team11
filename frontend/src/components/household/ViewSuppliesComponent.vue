<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';
import { InventoryService } from '@/api/InventoryService';
import { useHouseholdStore } from '@/stores/householdStore';
import { HouseholdService } from '@/api/HouseholdService';
import { ItemService } from '@/api/ItemService';
import type { HouseholdItemRequest } from '@/types/Inventory';
import { formatDate } from '@/utils/formatDate';

const householdStore = useHouseholdStore();

const itemTypeStore = useItemTypeStore();
// Id of the selected item type
const selectedTypeId = ref<number | null>(null)
// Is edit mode enabled
const isEditMode = ref(false);
// List of items in the inventory
const list = ref<{ id: number; name: string; quantity: number; unit: string; acquiredDate: string }[]>([]);

onMounted( async () => {
    try {
        // Get actual household id from the store
        const household = await HouseholdService.findById(1);
        householdStore.setHousehold({id: household.id, name: household.name, memberCount: household.memberCount, addressId: household.address.id.toString()});
        
        if (!household) {
            console.error('Household ID is not available');
            return [];
        }

        // Fetch inventory items from the service
        const items = await InventoryService.list(household.id);
        if (!items) {
            console.error('No items found in the inventory');
            return [];
        }

        // Fetch item names for each item in the inventory
        const itemTypes = await Promise.all(
            items.map(item => ItemService.findById(item.itemId))
        )

        list.value = items.map((item, index) => ({
            id: item.itemId,
            name: itemTypes[index].name,
            quantity: item.quantity,
            unit: item.unit,
            acquiredDate: item.acquiredDate
        }));
    } catch (error) {
        console.error('Failed to load inventory:', error);
    }
});

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
const deleteItem = (itemId: number, acquiredDate: string) => {
    try {
        if (!householdStore.id) {
            console.error('Household ID is not available');
            return;
        }
        InventoryService.remove(householdStore.id, itemId, acquiredDate);
        list.value = list.value.filter(item => item.id !== itemId);
    } catch (error) {
        console.error('Failed to delete item:', error);
    }
}
</script>
<template>
    <h1 class="medium-header">Beredskapslager</h1>

    <div class="grey-container">
        <div v-for="item in list" :key="item.id" class="item-card">
            <div v-if="isEditMode" class="delete-button" @click="deleteItem(item.id, item.acquiredDate)">X</div>
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
        height: 6rem;
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