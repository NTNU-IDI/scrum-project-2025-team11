<script lang="ts" setup>
import { UserService } from '@/api/UserService';
import { onMounted, ref, watch } from 'vue';
import { useAdminUserStore } from '@/stores/adminUserStore';

/*
const adminbrukere = ref([
    {id: 1, username: 'Garv', email: 'garv@admin.com'},
    {id: 2, username: 'An', email: 'an@admin.com'},
    {id: 3, username: 'Therese', email: 'therese@admin.com'},
    {id: 4, username: 'Jonas', email: 'jonas@admin.com'},
    {id: 5, username: 'Matteus', email: 'matteus@admin.com'},
    {id: 6, username: 'August', email: 'august@admin.com'},
    {id: 7, username: 'Gabriel', email: 'gabriel@admin.com'},
    {id: 8, username: 'Madde', email: 'madde@Admin.com'},
]);
*/

// Store imports
const adminUserStore = useAdminUserStore();

// Props
const adminbrukere = ref<{ id: number; username: string; email: string }[]>([]);

const loadUsers = async () => {
    await adminUserStore.fetchUsers();
};

onMounted( async () => {
    await loadUsers();
});

watch(() => adminUserStore.adminUsers, async (newUsers) => {
    adminbrukere.value = newUsers.filter(user => user.role === 'admin').map(user => ({
        id: user.id,
        username: user.username,
        email: user.email
    }));
});

const deleteUser = (id: number) => {
   // TODO
};
</script>
<template>
<div class="page-container">
    <div class="header-box-container">
        <h1 class="medium-header">Adminbrukere</h1>
        <div class="grey-container">
            <div class="header-cards">
                <div class="article-card" id="id-header">ID</div>
                <div class="article-card" id="username-header">Brukernavn</div>
                <div class="article-card" id="email-header">Epost</div>
                <div class="article-card" id="delete-header">Slett</div>
            </div>
            <div v-for="(bruker, index) in adminbrukere" :key="index" class="user-card">
                <p class="article-card" id="id">{{ bruker.id }}</p>
                <h2 class="article-card" id="username">{{ bruker.username }}</h2>
                <p class="article-card" id="email">{{ bruker.email }}</p>
                <button class="delete-button">X</button>
            </div>
        </div>
    </div>
</div>
</template>
<style scoped>
.page-container {
    margin: 1rem;
}

.grey-container {
    width: 45rem;
}

.user-card, .header-cards {
    display: flex;
    flex-direction: row;
    align-items: center;
}
.article-card {
    display: flex; 
    height: 2rem;
    padding: 0.5rem;
    text-align: center;
    font-weight: normal;
    margin: 0.5rem;
    font-size: var(--font-size-medium); 
}

#id-header, #username-header, #email-header {
    background-color: var(--light-blue);
    color: white;
}

#delete-header {
    color: var(--darkest-blue);
    background-color: transparent;
    border: none;
}

.delete-button {
    background-color: transparent;
    border: none;
    color: var(--bad-red);
    width: 3.05rem;
    padding-left: 1.5rem;
}

#id-header, #id {
    width: 2rem;
    justify-content: center; 
    align-items: center;
}
#username-header, #username {
    width: 15rem;
}

#email-header, #email {
    width: 20rem;
}
#delete-header, #delete {
    width: 2rem;
    justify-content: center; 
    align-items: center;
}
</style>