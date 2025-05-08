<script setup lang="ts">
    import HeaderBase from "@/components/HeaderBase.vue";
    import Footer from "@/components/Footer.vue";
    import PreparationsComponent from "@/components/PreparationsComponent.vue";
    import { useRoute } from "vue-router";
    import { computed } from "vue";

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

    const formatStage = (s: string) =>
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

    @media(max-width: 480px) {
        #preparationsContainer {
            flex-direction: column; /* Stack elements vertically */
            align-items: center; /* Center align items */
        }

        #previous, #next {
            width: 100%; /* Make buttons take full width */
            display: inline-block;
            justify-content: center; /* Center align buttons */
            margin-bottom: 20px;
            padding: 0;
            align-items: center;
            margin-left: 10%;
        }
        #previous {
            order: -2;
        }

        #next {
            order: -1;
        }

        .box {
            width:80%; /* Reduce button width for smaller screens */

        }
    }
</style>