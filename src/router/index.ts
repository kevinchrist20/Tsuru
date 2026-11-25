import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import ToolView from '../views/tools/ToolView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Dashboard',
      component: DashboardView,
    },
    {
      path: '/tools/:id',
      name: 'Tool',
      component: ToolView,
    },
    {
      // Catch-all route for any unmatched routes
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
})

export default router
