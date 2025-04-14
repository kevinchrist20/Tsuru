import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import Base64View from '../views/tools/Base64View.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Dashboard',
      component: DashboardView
    },
    {
      path: '/tools/:id',
      name: 'Tool',
      component: Base64View,
      props: true
    },
    {
      // Catch-all route for any unmatched routes
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
})

export default router
