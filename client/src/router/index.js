import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      alias: '/schedules',
      name: 'Schedules',
      component: () => import('@/views/SchedulesView.vue'),
    },
    {
      path: '/categories',
      name: 'Categorie',
      component: () => import('@/views/CategoriesView.vue'),
    },
    {
      path: '/users',
      name: 'Users',
      component: () => import('@/views/UsersView.vue'),
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'Exception',
      component: () => import('@/views/ExceptionView.vue'),
    },
  ],
})

export default router
