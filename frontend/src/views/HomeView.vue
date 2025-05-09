<template>
  <div class="page-wrapper">
    <HeaderBase />
    <HomeBase id="homebase"/>
    <Footer />
  </div>
</template>

<script lang="ts" setup>
import HeaderBase from '@/components/HeaderBase.vue'
import HomeBase from '@/components/HomeBase.vue';
import Footer from '@/components/Footer.vue'
</script>

<style scoped>
  homebase {
    padding: 20px 0;
  }
</style>
