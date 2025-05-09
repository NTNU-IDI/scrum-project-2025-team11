<script lang="ts" setup>
import HeaderBase from '@/components/HeaderBase.vue'
import Footer from '@/components/Footer.vue'
import ManageUsersComponent from '@/components/admin/ManageUsersComponent.vue'
import HomeBase from '@/components/homepages/HomeBase.vue'
</script>
<template>
<HeaderBase />
<div class="page-container">
    <ManageUsersComponent />
</div>
<Footer />
</template>
