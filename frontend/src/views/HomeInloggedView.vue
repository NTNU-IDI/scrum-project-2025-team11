<template>
  <div class="page-wrapper">
    <UserHeader />
    <Userhome />
    <Footer />
  </div>
</template>

<script lang="ts" setup>
import UserHeader from '@/components/HeaderInloggedComponent.vue'
import Userhome from '@/components/homepages/HomeInloggedComponent.vue'
import Footer from '@/components/Footer.vue'
</script>
