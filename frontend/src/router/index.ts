import HomeView from "@/views/HomeView.vue";
import LogInView from "@/views/LogInView.vue";
import MapView from "@/views/MapView.vue";
import RegisterView from "@/views/RegisterView.vue";
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
  {
    path: "/register",
    name: "RegisterView",
    component: RegisterView,
  },
  {
    path: "/map",
    name: "MapView",
    component: MapView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
