<script setup lang="ts">
    import HeaderBase from "@/components/HeaderBase.vue";
    import Footer from "@/components/Footer.vue";
    import PreparationsComponent from "@/components/PreparationsComponent.vue";
    import { useRoute } from "vue-router";
    import { computed, ref } from "vue";

    const route = useRoute()
    const stage = computed(() => Array.isArray(route.params.stage) ? route.params.stage[0] : route.params.stage);

    const stages = ['før', 'under', 'etter'];
    const currentIndex = computed(() => stages.indexOf(stage.value));
    console.log(currentIndex);

    const prevStage = computed(() =>
    currentIndex.value > 0 ? stages[currentIndex.value - 1] : null
    )

    const nextStage = computed(() =>
    currentIndex.value < stages.length - 1 ? stages[currentIndex.value + 1] : null
    )

    const formatStage = (s) =>
    s.charAt(0).toUpperCase() + s.slice(1)
</script>

<template>
    <HeaderBase />
    <!--<div class="prev_and_next">
        <div id="previous">
            <router-link
                v-if="prevStage"
                :to="`/beredskap/${prevStage}`"
            >
                <button class="box">
                    <i class="fa fa-arrow-left" aria-hidden="true"></i> {{ formatStage(prevStage) }}
                </button>
            </router-link>
        </div>

        <div id="next">
            <router-link
                v-if="nextStage"
                :to="`/beredskap/${nextStage}`"
            >
                <button class="box">
                    {{ formatStage(nextStage) }} <i class="fa fa-arrow-right" aria-hidden="true"></i>
                </button>
            </router-link>
        </div>
    </div>-->
    <div id="preparationsContainer">
        <div id="previous">
            <router-link
                v-if="prevStage"
                :to="`/beredskap/${prevStage}`"
            >
                <button class="box">
                    <i class="fa fa-arrow-left" aria-hidden="true"></i> {{ formatStage(prevStage) }}
                </button>
            </router-link>
        </div>
        <PreparationsComponent :stage="stage" />
        <div id="next">
            <router-link
                v-if="nextStage"
                :to="`/beredskap/${nextStage}`"
            >
                <button class="box">
                    {{ formatStage(nextStage) }} <i class="fa fa-arrow-right" aria-hidden="true"></i>
                </button>
            </router-link>
        </div>
    </div>
    <Footer />
</template>

<style scoped>
    #preparationsContainer {
        height: 100%;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        padding: 20px 0;
        
    }

    .prev_and_next {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 100%;
    }

    #previous {
        align-self: flex-start;
        padding: 10px 30px 0 0;
        width: 140px;
        display: flex;
        justify-content: flex-end;
        
    }
    #next {
        align-self: flex-start;
        padding: 10px 0 0 30px;
        width: 140px;
    }
    
    .box {
        width: 120px;
        text-align: center;
    }
</style>