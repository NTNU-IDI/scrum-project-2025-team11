<script lang="ts" setup>
import { onMounted } from 'vue';
import { useHouseholdStore } from '@/stores/householdStore';

/**
 *  Store imports
 * @property {import('@/stores/householdStore').HouseholdStore}
 */
const householdStore = useHouseholdStore();

// Fetch household when the component is mounted
onMounted( async () => {
    await householdStore.fetchHousehold();
});

</script>
<template>
    <div class="page-container">
        <div class="info-container">
            <div class="me-container">
                <h1 class="medium-header">Navn</h1>
                <div class="article-card">
                    <p>{{ householdStore.name }}</p>
                </div>
            </div>

            <div class="address-container">
                <h1 class="medium-header">Adresse</h1>
                <div class="article-card">
                    <p id="address">{{ householdStore.address }}</p>
                </div>
            </div>

            <div class="members-container">
                <h1 class="medium-header">Medlemmer</h1>
                <div class="members-cards">
                    <div v-for="member in householdStore.members" :key="member.email" class="article-card">
                        <p>{{ member.firstName + ' ' + member.lastName }}</p>
                    </div>
                </div>
            </div>

            <div class="button-container">
                <button class="dark-button" id="invite-button"
                    @click="$emit('show-new-member-box')"
                >
                    Inviter
                </button>
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

    .medium-header {
        font-size: var(--font-size-large);
        text-align: left;
    }

    .info-container {
        display: flex;
        flex-direction: row;
        gap: 10rem;
        width: 100%;
    }

    .info-container {
        display: flex;
        flex-direction: row;
        gap: 5rem;
        width: 100%;
    }

    .me-container, .address-container, .members-container {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }  
    
    .members-cards {
        display: flex;
        flex-direction: row;
        gap: 1rem;
        overflow-x: auto;
        height: 5rem;
        padding: 0 0.5rem 0 0.5rem ;
        scrollbar-width: thin;
    }

    .article-card {
        background-color: var(--grey);
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        border: none;
        min-width: 8vw;
        max-width: 15rem;
        width: auto;
    }

    .button-container {
        margin-left: auto; 
        margin-right: -2.7rem;
        margin-top: 4.5rem;
        align-self: flex-start;
        height: 8rem;
    }

    .dark-button {
        height: 4vw;
        width: 11vw;
    }

    .dark-button:disabled {
        opacity: 0.5;
        cursor: not-allowed;
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

        .info-container {
          flex-direction: column !important;
          gap: 0px !important;
        }

        .dark-button {
          width: 100% !important;
          height: 3rem !important;
        }
    }
</style>