<script setup lang="ts">
    import HeaderBase from "@/components/HeaderBase.vue";
    import Footer from "@/components/Footer.vue";
    import PreparationsComponent from "@/components/PreparationsComponent.vue";
    import { useRoute } from "vue-router";

    const route = useRoute()
    const stage = Array.isArray(route.params.stage) ? route.params.stage[0] : route.params.stage;
</script>

<template>
    <HeaderBase />
    <div id="preparationsContainer">
        <PreparationsComponent :stage="stage" />
    </div>
    <Footer />
</template>

<style scoped>
    #preparationsContainer {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20px 0;
    }
</style>