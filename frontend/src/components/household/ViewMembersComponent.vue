<script lang="ts" setup>
import { computed, ref } from 'vue';
import { useUserStore } from '@/stores/userStore';

const userStore = useUserStore();
// TODO: change store or get user by token

const myName = ref('Madde');
const householdMembers = ref([
    { id: 1, name: 'Lars Larsen' },
    { id: 2, name: 'Mamma Mammasen' },
    { id: 3, name: 'Pappa Papapp' },
    { id: 4, name: 'Søster Søster' },
    { id: 5, name: 'Bror Bror' },
    { id: 6, name: 'Bestemor Bestemor' },
    { id: 7, name: 'Bestefar Bestefar' },
    { id: 8, name: 'Onkel Onkel' },
    { id: 9, name: 'Tante Tante' },
    { id: 10, name: 'Fetter Fetter' },
    { id: 11, name: 'Kusine Kusine' },
    { id: 12, name: 'Sønn Sønn' },
    { id: 13, name: 'Datter Datter' },
    { id: 14, name: 'Barnebarn Barnebarn' },
    { id: 15, name: 'Oldefar Oldefar' },
    { id: 16, name: 'Oldemor Oldemor' },
]);

const addMember = () => {
    //TODO: add member to household
}

</script>
<template>
    <div class="page-container">
        <div class="members-container">
            <div class="me-container">
                <h1 class="medium-header">Deg</h1>
                <div class="article-card">{{ myName }}</div>
            </div>

            <div class="other-members-container">
                <h1 class="medium-header">Andre medlemmer</h1>
                <div class="other-members-cards">
                    <div v-for="member in householdMembers" :key="member.id" class="article-card">
                        <p>{{ member.name }}</p>
                    </div>
                </div>
            </div>
            <button class="dark-button" @click="() => { addMember(); $emit('show-new-member-box'); }">+</button>
        </div>    
    </div>
</template>
<style scoped>
    .page-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: flex-start;
        padding: 1rem;
    }

    .members-container {
        display: flex;
        flex-direction: row;
        gap: 10rem;
        width: 100%;
    }

    .me-container, .other-members-container {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }      

    .other-members-cards {
        display: flex;
        flex-direction: row;
        gap: 1rem;
        overflow-x: auto;
        max-width: 50rem;
        height: 5rem;
        padding: 0.5rem;
        scrollbar-width: thin;
    }

    .article-card {
        background-color: var(--grey);
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        border: none;
        width: auto;
    }
    .dark-button {
        height: 3rem;
        width: 3rem;
        font-size: var(--font-size-xlarge);
        margin-left: auto; 
        margin-top: 4.75rem;
        align-self: flex-start;
    }
</style>