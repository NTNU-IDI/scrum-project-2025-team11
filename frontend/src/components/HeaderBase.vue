<script setup lang="ts">
import Header from "@/components/Header.vue";
import HeaderInloggedComponent from "@/components/HeaderInloggedComponent.vue";
import HeaderAdminComponent from "@/components/HeaderAdminComponent.vue";
import HeaderSuperAdminComponent from "@/components/HeaderSuperAdminComponent.vue";
import {useUserStore} from "@/stores/userStore.ts";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const {role} = storeToRefs(userStore)

</script>

<template>
  <Header v-if="role === ''"/>
  <HeaderInloggedComponent v-if="role === 'normal'" />
  <HeaderAdminComponent v-if="role === 'admin'" />
  <HeaderSuperAdminComponent v-if="role === 'super_admin'" />
</template>

<style scoped>

</style>