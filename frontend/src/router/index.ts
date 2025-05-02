import HomeView from "@/views/HomeView.vue";
import LogInView from "@/views/LogInView.vue";
import MapView from "@/views/map/MapView.vue";
import RegisterView from "@/views/RegisterView.vue";
import HouseholdView from "@/views/household/HouseholdView.vue";
import PrivacyPolicyView from "@/views/PrivacyPolicyView.vue";
import AuthView from "@/views/AuthView.vue";
import UserHomeView from "@/views/HomeInloggedView.vue";
import type { RouteRecordRaw } from "vue-router";
import { createRouter, createWebHistory } from "vue-router";
import {useUserStore} from "@/stores/userStore.ts";

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
    meta: { requiresNormalUser: true }
  },
  {
    path: "/personvern",
    name: "PrivacyPolicy",
    component: PrivacyPolicyView,
  },
  {
    path: "/userhome",
    name: "UserHomeView",
    component: UserHomeView,
  },
  {
    path: "/auth",
    name: "Auth",
    component: AuthView,
  },
  // { //Eksempel - admin-route
  //   path: '/adminrute',
  //   name: 'AdminEksempelView',
  //   component: AdminEksempelView,
  //   meta: {requieresAdmin: true}
  // },
  // { //Eksempel - super-admin-route
  //   path: '/superadminrute',
  //   name: 'SuperAdminEksempelView',
  //   component: SuperAdminEksempelView,
  //   meta: {requieresSuperAdmin: true}
  // },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if(to.meta.requiresNormalUser && userStore.role !== 'normal') {
    alert("Du har ikke adgangsnivå til denne siden.")
    next("/")
  } else if (to.meta.requiresAdmin && userStore.role !== 'admin') {
    alert("Du har ikke adgangsnivå til denne siden.")
    next("/")
  } else if (to.meta.requiresSuperAdmin && userStore.role !== 'super_admin') {
    alert("Du har ikke adgangsnivå til denne siden.")
    next("/")
  } else {
    next()
  }
})

export default router;
