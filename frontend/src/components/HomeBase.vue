<script setup lang="ts">

import HomeView from "@/views/HomeView.vue";
import HomeInloggedView from "@/views/HomeInloggedView.vue";
import HomeAdminView from "@/views/HomeAdminView.vue";
import HomeSuperAdminView from "@/views/HomeSuperAdminView.vue";

import {useUserStore} from "@/stores/userStore.ts";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const {role} = storeToRefs(userStore)

</script>

<template>
  <HomeView v-if="role === ''" />
  <HomeInloggedView v-if="role === 'normal'" />
  <HomeAdminView v-if="role === 'admin'" />
  <HomeSuperAdminView v-if="role === 'super_admin'" />
</template>

<style scoped>

</style>