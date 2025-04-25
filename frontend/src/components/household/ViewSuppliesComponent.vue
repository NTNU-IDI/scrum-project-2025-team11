<script lang="ts" setup>
import { ref } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';

// Remove later
const items = ref([
    { id: 1, name: 'Vann', quantity: 10, unit: 'liter',
        items: [{quantity: 6, unit: 'kg', expirationDate: new Date('2026-10-01')}, {quantity: 1, unit: 'kg', expirationDate: new Date('2026-05-01')}]
    },
    { id: 2, name: 'Hermetiske tomater', quantity: 6, unit: 'kg',
        items: [{quantity: 6, unit: 'kg', expirationDate: new Date('2026-10-01')}, {quantity: 1, unit: 'kg', expirationDate: new Date('2026-05-01')}]
    },
    { id: 3, name: 'Medisin', quantity: 20, unit: 'tabletter',
        items: [{quantity: 6, unit: 'kg', expirationDate: new Date('2026-10-01')}, {quantity: 1, unit: 'kg', expirationDate: new Date('2026-05-01')}]
    },
    { id: 6, name: 'Hermetiske bønner', quantity: 1, unit: 'kg', items: []},
    { id: 7, name: 'Havregryn', quantity: 1, unit: 'kg', items: []},
    { id: 8, name: 'Ris', quantity: 1, unit: 'kg', items: []},
    { id: 9, name: 'Pasta', quantity: 1, unit: 'kg', items: []},
]);

const itemTypeStore = useItemTypeStore();
// Id of the selected item type
const selectedTypeId = ref<number | null>(null)
// Is edit mode enabled
const isEditMode = ref(false);

// TODO: get total number of items by type
// const items = getItemsByType()

// Choose an item in the supply
const chooseItemType = (itemTypeId: any, name: string, items: any[]) => {
    selectedTypeId.value = itemTypeId;
    itemTypeStore.setItemType(itemTypeId, name, items);
}

// Toggle edit mode
const toggleEditMode = () => {
    itemTypeStore.toggleEditMode();
    isEditMode.value = !isEditMode.value;
}
</script>
<template>
    <h1 class="medium-header">Beredskapslager</h1>

    <div class="grey-container">
        <div v-for="item in items" :key="item.id" class="item-card">
            <div v-if="isEditMode" class="delete-button">X</div>
            <div :class="['article-card', { active: item.id === selectedTypeId }]" @click="chooseItemType(item.id, item.name, item.items)">
                <div class="quantity">{{ item.quantity }} {{ item.unit }}</div>
                <div class="info">
                    <h2>{{ item.name }}</h2>
                </div>
            </div>
        </div>
        <div v-if="isEditMode" class="dark-button" id="add-button">+</div>
    </div>

    <button :class="['dark-button', { active: isEditMode }]" @click="toggleEditMode()">
        {{ isEditMode ? 'Large' : 'Endre lager' }}
    </button>
</template>
<style scoped>
    .quantity {
        display: flex;
        align-items: center;
    }

    .dark-button {
        height: 4rem;
        width: 10rem;
    }
    .dark-button.active {
        background-color: var(--good-green);
    }

    #add-button {
        display: flex;
        align-items: center; 
        justify-content: center; 
        align-self: right;
        width: 1.5rem; 
        height: 2rem; 
        padding-bottom: 0.7rem;
        font-size: var(--font-size-xlarge);
        margin-left: auto;
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