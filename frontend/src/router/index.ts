import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import HouseholdView from '@/views/household/HouseholdView.vue'

const routes: Array<RouteRecordRaw> = [
    {
      path: '/',
      name: 'Home',
      component: HomeView,
    },
    {
      path: '/household',
      name: 'Household',
      component: HouseholdView,
    }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router