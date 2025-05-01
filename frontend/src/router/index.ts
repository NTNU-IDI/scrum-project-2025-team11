import HomeView from "@/views/HomeView.vue";
import LogInView from "@/views/LogInView.vue";
import MapView from "@/views/map/MapView.vue";
import RegisterView from "@/views/RegisterView.vue";
import HouseholdView from "@/views/household/HouseholdView.vue";
import PrivacyPolicyView from "@/views/PrivacyPolicyView.vue";
import AuthView from "@/views/AuthView.vue";
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
  {
    path: "/household",
    name: "Household",
    component: HouseholdView,
  },
  {
    path: '/personvern',
    name: 'PrivacyPolicy',
    component: PrivacyPolicyView,
  },
  {
    path: '/auth',
    name: 'Auth',
    component: AuthView,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
