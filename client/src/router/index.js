import { createRouter, createWebHistory } from 'vue-router'
import { Role } from '../enums/Role'
import { useAuthenticationStore } from '../stores/authentication'

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
      path: '/create-event',
      name: 'Create Event',
      component: () => import('@/views/CreateEventView.vue'),
    },
    {
      path: '/schedules/:bookingId',
      name: 'Edit Event',
      props: true,
      component: () => import('@/views/EditEventView.vue'),
      meta: {
        requireAuth: true,
        allowRoles: [Role.Admin, Role.Student],
      },
    },
    {
      path: '/categories',
      name: 'Categorie',
      component: () => import('@/views/CategoriesView.vue'),
      meta: {
        requireAuth: true,
        allowRoles: [Role.Admin, Role.Lecturer, Role.Student],
      },
    },
    {
      path: '/users',
      name: 'Users',
      component: () => import('@/views/UsersView.vue'),
      meta: {
        requireAuth: true,
        allowRoles: [Role.Admin],
      },
    },
    {
      path: '/users/:userId',
      name: 'Edit User',
      component: () => import('@/views/EditUserView.vue'),
      meta: {
        requireAuth: true,
        allowRoles: [Role.Admin],
      },
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'Exception',
      component: () => import('@/views/ExceptionView.vue'),
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthenticationStore()
  const requireAuth = to.meta?.requireAuth
  const allowRoles = to.meta?.allowRoles || []

  // Always looking for authentication
  const { accessToken, refreshToken } = authStore.getToken()

  if (accessToken || refreshToken) {
    try {
      await authStore.retrieveUser()
    } catch (err) {
      authStore.deleteToken()
      return next('/')
    }
  }

  if (requireAuth && !authStore.isAuthenticated) {
    return next('/')
  }

  if (requireAuth && authStore.isAuthenticated) {
    if (!allowRoles.includes(authStore.user?.role)) {
      next('/')
      return
    }
  }

  return next()
})

export default router
