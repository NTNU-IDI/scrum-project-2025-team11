<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue';
import { useAdminUserStore } from '@/stores/adminUserStore';
import { useToast } from 'vue-toast-notification';

// Store imports
const adminUserStore = useAdminUserStore();

// Props
const adminUsers = ref<{ id: number; firstName: string; lastName: string; username: string; email: string }[]>([]);
const $toast = useToast();

const loadUsers = async () => {
    await adminUserStore.fetchUsers();
};

onMounted( async () => {
    await loadUsers();
});

watch(() => adminUserStore.adminUsers, async (newUsers) => {
    adminUsers.value = newUsers.map(user => ({
        id: user.id,
        firstName: user.firstName,
        lastName: user.lastName,
        username: user.username,
        email: user.email
    }));
});

const deleteUser = async (id: number) => {
    if(confirm('Er du sikker på at du vil slette denne adminbrukeren?')) {
        await adminUserStore.deleteUser(id);
        if(adminUserStore.adminUsers.map(user => user.id).includes(id)) {
            if(adminUserStore.errorMsg !== '') {
                $toast.error(adminUserStore.errorMsg, {
                    duration: 3000,
                    position: 'top-right'
                });
            }
        } else {
            $toast.success('Bruker slettet', {
                duration: 3000,
                position: 'top-right'
            });
        }
    }
};
</script>
<template>
<div class="page-container">
    <div class="header-box-container">
        <h1 class="medium-header">Adminbrukere</h1>
        <div class="grey-container">
            <div class="header-cards">
                <div class="article-card" id="id-header">ID</div>
                <div class="article-card" id="name-header">Fullt navn</div>
                <div class="article-card" id="username-header">Brukernavn</div>
                <div class="article-card" id="email-header">Epost</div>
                <div class="article-card" id="delete-header">Slett</div>
            </div>
            <div v-for="(user, index) in adminUsers" :key="index" class="user-card">
                <p class="article-card" id="id">{{ user.id }}</p>
                <p class="article-card" id="name">{{ user.firstName + ' ' + user.lastName}}</p>
                <h2 class="article-card" id="username">{{ user.username }}</h2>
                <p class="article-card" id="email">{{ user.email }}</p>
                <button class="delete-button" @click="deleteUser(user.id)">X</button>
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
    width: 60vw;
    height: auto;
}

h1 {
    text-align: left;
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

#id-header, #name-header, #username-header, #email-header {
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

.delete-button:hover {
    background-color: transparent;
}

#id-header, #id {
    width: 2rem;
    justify-content: center; 
    align-items: center;
}
#username-header, #username, #name-header, #name {
    width: 15vw;
}

#email-header, #email {
    width: 20vw;
}
#delete-header, #delete {
    width: 2rem;
    justify-content: center; 
    align-items: center;
}

@media(max-width: 480px) {
    .grey-container {
        width: 100vw;
        width: 100%;
        margin: 0;
        overflow-y: hidden;
    }
    .article-card {
        font-size: x-small;
    }

    #id, #id-header {
        min-width: 1rem;
    }

    #name, #name-header, #username, #username-header, #email, #email-header {
        min-width: 9rem;
    }
    
}
</style>