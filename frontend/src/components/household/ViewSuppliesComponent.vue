<script lang="ts" setup>
import { ref } from 'vue';

const articles = ref([
    { id: 1, name: 'Vann', quantity: 10, unit: 'liter' , expirationDate: new Date('2025-12-01') },
    { id: 2, name: 'Hermetiske tomater', quantity: 5, unit: 'kg', expirationDate: new Date('2026-10-01') },
    { id: 3, name: 'Medisin', quantity: 20, unit: 'tabletter', expirationDate: new Date('2025-5-10') },
]);
</script>
<template>
    <h1>Beredskapslager</h1>
    <div class="supply-container">
        <div v-for="article in articles" :key="article.id">
            <div class="article-card">
                <p>{{ article.quantity }} {{ article.unit }}</p>
                <h2>{{ article.name }}</h2>
            </div>
        </div>
    </div>
</template>