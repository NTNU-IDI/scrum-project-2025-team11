<script lang="ts" setup>
import { computed, ref } from 'vue';
import { useItemTypeStore } from '@/stores/itemStore';
import { formatDate, formatDateToList } from '@/utils/formatDate';

// Remove later
const itemsDummy = ref([
    { name: 'Hermetiske tomater', quantity: 6, unit: 'kg', expirationDate: new Date('2026-10-01') },
    { name: 'Hermetiske tomater', quantity: 1, unit: 'kg', expirationDate: new Date('2026-05-01') },
]);

const itemTypeStore = useItemTypeStore();
const itemTypeId = computed(() => itemTypeStore.id);
const itemTypeName = computed(() => itemTypeStore.name);
const items = computed(() => itemTypeStore.items);
//const isEditMode = itemTypeStore.isEditMode;
const isEditMode = computed(() => itemTypeStore.isEditMode);

// TODO: get household items by type
// const items = getHouseholdItems(itemTypeId)


// TODO: updateItems

const deleteItem = () => {
    // TODO: deleteItem
}

</script>
<template>
    <h1 class="medium-header">Informasjon om artikkel</h1>

    <!-- Edit mode -->
    <div class="grey-container" v-if="isEditMode">
        <h2 class="small-header">{{ itemTypeName }}</h2>
        <div v-for="item in items" :key="item.expirationDate.toLocaleDateString" class="item-card">
            <div class="delete-button" @click="deleteItem()">X</div>
            <div class="article-card">
                <input type="text" class="edit-input" id="quantity-input" v-model="item.quantity" /> 
                <input type="text" class="edit-input" v-model="item.unit" />
                <div class="info">
                    <div class="exp-date">
                        <span class="grey-text">Utløper: </span> 
                        <input type="text" class="edit-input" id="year-input" v-model="formatDateToList(item.expirationDate)[0]" />-
                        <input type="text" class="edit-input" id="month-input" v-model="formatDateToList(item.expirationDate)[1]" />-
                        <input type="text" class="edit-input" id="day-input" v-model="formatDateToList(item.expirationDate)[2]" />
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- View mode -->
    <div class="grey-container" v-else>
        <h2 class="small-header">{{ itemTypeName }}</h2>
        <div v-for="item in items" :key="item.expirationDate.toLocaleDateString">
            <div class="article-card">
                <p>{{ item.quantity }} {{ item.unit }}</p>
                <div class="info">
                    <div class="exp-date">
                        <span class="grey-text">Utløper: </span> {{ formatDate(item.expirationDate) }}
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
        text-align: right;
    }

    .article-card {
        border: 2px solid white; 
        background-color: white;
    }

    .grey-text {
        color: grey;
        padding: 0.1rem;
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
        min-width: 1.2rem;
        max-width: 2rem;
        margin-right: 0.2rem;
    }

    #year-input {
        min-width: 2.2rem;      
    }

    #month-input {
        min-width: 1.2rem;      
    }

    #day-input {
        min-width: 1.2rem;      
    }
    .exp-date {
        display: flex;
        align-items: center;
    }

</style>