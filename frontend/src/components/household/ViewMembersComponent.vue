<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { useHouseholdStore } from '@/stores/householdStore';
import { storeToRefs } from 'pinia';
import { HouseholdService } from '@/api/HouseholdService';

/**
 *  Store imports
 * @property {import('@/stores/householdStore').HouseholdStore}
 */
const householdStore = useHouseholdStore();

/**
 * Property to store member count of the household
 * @property {number} newMemberCount
 * @default 0
 */
const newMemberCount = ref(0);

/**
 * Property to store the response message
 * @property {string} responseMessage
 * @default ''
 */
const responseMessage = ref('');

// Fetch household and set member count when the component is mounted
onMounted( async () => {
    await householdStore.fetchHousehold();
    newMemberCount.value = householdStore.memberCount;
});

// Watch for changes in the member count
const hasChanges = computed(() => {
    responseMessage.value = '';
    return newMemberCount.value !== householdStore.memberCount;
});

/**
 * Function to update the member count of the household
 * @returns {Promise<void>}
 */
const changeMemberCount = async () => {
    try{
        if(!householdStore.id){
            responseMessage.value = 'Kunne ikke finne husstand';
            return;
        }
        await householdStore.setMemberCount(newMemberCount.value); 
        responseMessage.value = `${newMemberCount.value - householdStore.memberCount} medlemmer er lagt til`;
    } catch (error) {
        responseMessage.value = 'Kunne ikke oppdatere husstand';
    }
}

</script>
<template>
    <div class="page-container">
        <div class="members-container">
            <div class="me-container">
                <h1 class="medium-header">Deg</h1>
                <div class="article-card">
                    <p>{{ householdStore.name }}</p>
                </div>
            </div>

            <div class="other-members-container">
                <h1 class="medium-header">Antall medlemmer</h1>
                <input class="article-card" id="members-input" 
                    type="number" 
                    min="0" 
                    step="1" 
                    aria-label="Quantity" 
                    v-model="newMemberCount"
                ></input>
            </div>

            <div class="button-container">
                <button class="dark-button" 
                    @click="changeMemberCount()"
                    :disabled="!hasChanges"
                >
                    Lagre
                </button>
                <button class="dark-button" id="invite-button"
                    @click="$emit('show-new-member-box')"
                >
                    Inviter
                </button>
                <p class="user-response">{{ responseMessage }}</p>
            </div>
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

    @media(max-width: 600px) {
      .page-container {
        padding: 0px;
      }
      .members-container {
        flex-direction: column !important;
        padding: 0px !important;
        gap: 1rem !important;
      }
      .button-container {
        margin: 0px !important;
      }
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

    #members-input {
        height: 3.5rem;
        width: 5rem;
        text-align: center;
    }

    .button-container {
        margin-left: auto; 
        margin-top: 4.75rem;
        align-self: flex-start;
        height: 8rem;
    }

    .dark-button {
        height: 3.5rem;
        width: 9rem;
        margin: 1rem;
        background-color: var(--light-blue);
    }

    .dark-button:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }

    #invite-button {
        background-color: var(--dark-blue);
    }
</style>