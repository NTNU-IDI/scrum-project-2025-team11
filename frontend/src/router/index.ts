import HomeView from "@/views/HomeView.vue";
import LogInView from "@/views/LogInView.vue";
import MapView from "@/views/map/MapView.vue";
import RegisterView from "@/views/RegisterView.vue";
import HouseholdView from "@/views/household/HouseholdView.vue";
import PrivacyPolicyView from "@/views/PrivacyPolicyView.vue";
import AuthView from "@/views/AuthView.vue";
import UserHomeView from "@/views/HomeInloggedView.vue";
import AdminView from "@/views/HomeAdminView.vue";
import SuperAdminView from "@/views/admin/SuperAdminView.vue";
import HomeAdminView from "@/views/HomeAdminView.vue";
import AboutView from "@/views/AboutView.vue";
import ForgotPassword from "@/views/ForgotPasswordView.vue";
import ResetPassword from "@/views/ResetPasswordView.vue";

import Headers from "@/views/AllHeaders.vue";

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
    meta: { requiresNormalUser: true }
  },
  {
    path: "/superadmin",
    name: "SuperAdminView",
    component: SuperAdminView,
    meta: { requiresSuperAdmin: true }
  },
  {
    path: "/admin",
    name: "AdminView",
    component: AdminView,
    meta: { requiresAdmin: true }
  },
  {
    path: "/auth",
    name: "Auth",
    component: AuthView,
  },
  {
    path: '/adminhome',
    name: 'HomeAdminView',
    component: HomeAdminView,
    meta: { requiresAdmin: true }
  },
  { path: '/about',
    name: 'AboutView',
    component: AboutView,
  },
  {
    path: "/forgot",
    name: "ForgotPassword",
    component: ForgotPassword,
  },
  {
    path: "/reset",
    name: "ResetPassword",
    component: ResetPassword,
  },


  {
    path: "/headers",
    name: "Headers",
    component: Headers,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Accesse 
/*
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
  */

export default router;
