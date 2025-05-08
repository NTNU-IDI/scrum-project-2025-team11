<script setup lang="ts">

import HomeNormalComponent from "@/components/homepages/HomeNormalComponent.vue";
import HomeInloggedComponent from "@/components/homepages/HomeInloggedComponent.vue";
import HomeAdminComponent  from "@/components/homepages/HomeAdminComponent.vue";
import HomeSuperAdminComponent  from "@/components/homepages/HomeSuperAdminComponent.vue";

import {useUserStore} from "@/stores/userStore.ts";
import {storeToRefs} from "pinia";

const userStore = useUserStore()
const {role} = storeToRefs(userStore)

</script>

<template>
  <HomeNormalComponent v-if="role === ''" />
  <HomeInloggedComponent v-if="role === 'normal'" />
  <HomeAdminComponent v-if="role === 'admin'" />
  <HomeSuperAdminComponent v-if="role === 'super_admin'" />
</template>

<style scoped>

</style>