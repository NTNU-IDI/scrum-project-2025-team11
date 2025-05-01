import HomeView from "@/views/HomeView.vue";
import LogInView from "@/views/LogInView.vue";
import MapView from "@/views/map/MapView.vue";
import RegisterView from "@/views/RegisterView.vue";
import HouseholdView from "@/views/household/HouseholdView.vue";
import PrivacyPolicyView from "@/views/PrivacyPolicyView.vue";
import UserHomeView from "@/views/HomeInloggedView.vue";
import AdminView from "@/views/admin/AdminView.vue";
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
    path: "/userhome",
    name: "UserHomeView",
    component: UserHomeView,
  },
  {
    path: "/admin",
    name: "AdminView",
    component: AdminView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
