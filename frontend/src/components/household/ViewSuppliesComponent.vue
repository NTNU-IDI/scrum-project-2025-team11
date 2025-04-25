<script lang="ts" setup>
import { ref } from 'vue';
import { useItemTypeStore } from '@/stores/item';

const itemStore = useItemTypeStore();

// TODO: get total number of items by type
// const items = getItemsByType()

const items = ref([
    { id: 1, name: 'Vann', quantity: 10, unit: 'liter'},
    { id: 2, name: 'Hermetiske tomater', quantity: 6, unit: 'kg'},
    { id: 3, name: 'Medisin', quantity: 20, unit: 'tabletter'},
]);

const selectedTypeId = ref<number | null>(null)

const chooseItemType = (itemTypeId: any) => {
    selectedTypeId.value = itemTypeId;
    itemStore.setItemType(itemTypeId);
}
</script>
<template>
    <h1 class="medium-header">Beredskapslager</h1>
    <div class="grey-container">
        <div 
        v-for="item in items" 
        :key="item.id" 
        @click="chooseItemType(item.id)"
        :class="['article-card', { active: item.id === selectedTypeId }]"
        >
            <div class="quantity">{{ item.quantity }} {{ item.unit }}</div>
            <div class="info">
                <h2>{{ item.name }}</h2>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .article-card.active, .article-card:hover {
        background-color: var(--light-blue); 
        color: white;
    }

    .article-card.active .quantity, .article-card:hover .quantity {
        color: white;
        background-color: transparent;
    }
</style>