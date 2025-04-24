import HomeView from "@/views/HomeView.vue";
import LogInView from "@/views/LogInView.vue";
import type { RouteRecordRaw } from "vue-router";
import { createRouter, createWebHistory } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: HomeView,
  },
  {
    path: "/login",
    name: "LogInView",
    component: LogInView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
