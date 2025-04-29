<script lang="ts" setup>
import { computed, ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useHouseholdStore } from '@/stores/householdStore';
import { storeToRefs } from 'pinia';

const userStore = useUserStore();
// Remove later
userStore.setUsername('Madde')

const useHousehold = useHouseholdStore();
// Remove later
useHousehold.setHousehold({id: 1, name: 'Familien Larsen', memberCount: 5, addressId: '1'});

const addMember = () => {
    //TODO: add member to household
}

</script>
<template>
    <div class="page-container">
        <div class="members-container">
            <div class="me-container">
                <h1 class="medium-header">Deg</h1>
                <div class="article-card">
                    <p>{{ userStore.username }}</p>
                </div>
            </div>

            <div class="other-members-container">
                <h1 class="medium-header">Antall medlemmer</h1>
                <p>{{ useHousehold.memberCount }}</p>
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