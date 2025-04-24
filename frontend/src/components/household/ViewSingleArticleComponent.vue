<script lang="ts" setup>
import { ref } from 'vue';

const articles = ref([
    { id: 1, name: 'Hermetiske tomater', quantity: 1, unit: 'kg' , expirationDate: new Date('2025-12-01') },
    { id: 2, name: 'Hermetiske tomater', quantity: 5, unit: 'kg', expirationDate: new Date('2026-10-01') },
]);
</script>
<template>
    <h1>Informasjon om artikkel</h1>
    <div class="supply-container">
        <h2>{{ articles[0].name }}</h2>
        <div v-for="article in articles" :key="article.id">
            <div class="article-card">
                <p>{{ article.quantity }} {{ article.unit }}</p>
                <h2>{{ article.expirationDate.toISOString }}</h2>
            </div>
        </div>
    </div>
</template>