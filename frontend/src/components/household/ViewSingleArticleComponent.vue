<script lang="ts" setup>
import { ref } from 'vue';
import { useItemTypeStore } from '@/stores/item';

const itemTypeStore = useItemTypeStore();
const itemTypeId = itemTypeStore.id

// TODO: get household items by type
// const items = getHouseholdItems(itemTypeId)

const items = ref([
    { name: 'Hermetiske tomater', quantity: 6, unit: 'kg', expirationDate: new Date('2026-10-01') },
    { name: 'Hermetiske tomater', quantity: 1, unit: 'kg', expirationDate: new Date('2026-05-01') },
]);
</script>
<template>
    <h1 class="medium-header">Informasjon om artikkel</h1>
    <div class="grey-container">
        <h2 class="small-header">{{ items[0].name }}</h2>
        <div v-for="item in items" :key="item.expirationDate.toLocaleDateString">
            <div class="article-card">
                <p>{{ item.quantity }} {{ item.unit }}</p>
                <div class="info">
                    <h2>
                        <span class="grey-text">Utløpsdato:</span> {{ item.expirationDate.toLocaleDateString() }}
                    </h2>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .grey-container {
        background-color: var(--light-blue);
    }

    .small-header {
        color: white;
        font-size: var(--font-size-large);
        font-weight: normal;
        text-align: right;
    }

    .article-card {
        border: 2px solid white; 
        background-color: white;
    }

    .grey-text {
        color: grey;
    }
</style>