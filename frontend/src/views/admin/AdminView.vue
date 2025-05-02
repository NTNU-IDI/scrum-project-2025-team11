<script lang="ts" setup>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import ManageUsersComponent from '@/components/admin/ManageUsersComponent.vue';
import ManageEventsComponent from '@/components/admin/ManageEventsComponent.vue';
</script>
<template>
<Header />
<div class="page-container">
    <ManageUsersComponent />
    <ManageEventsComponent />
</div>
<Footer />
</template>
