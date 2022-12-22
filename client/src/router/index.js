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
      path: '/categories/:categoryId',
      name: 'Edit Category',
      component: () => import('@/views/EditCategoryView.vue'),
      meta: {
        requireAuth: true,
        allowRoles: [Role.Admin],
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

  // Handle case - verify access token on every route change
  if (accessToken) {
    const validate = await authStore.verifyAccessToken()
    if (!validate) {
      if (!refreshToken) {
        authStore.logout()
        return next('/')
      }
      const { data, error } = await authStore.refreshToken()
      if (error) {
        authStore.logout()
        return next('/')
      }
    }
  }

  // Handle case - User with token tried to enter route but user state is empty
  if ((accessToken || refreshToken) && !authStore.isAuthenticated) {
    try {
      // when user state is empty, retrieve it
      await authStore.retrieveUser()
    } catch (err) {
      console.log('Access token expire')
      // any error founded revoke user token and re-route to root
      authStore.deleteToken()
      return next('/')
    }
  }

  // Handle case - when user tried to enter protected route without authenticated.
  if (requireAuth && !authStore.isAuthenticated) {
    return next('/')
  }

  if (requireAuth && authStore.isAuthenticated) {
    // Handle case - authenticated user tried to enter route without authorization allowance
    if (!allowRoles.includes(authStore.user?.role)) {
      next('/')
      return
    }
  }

  return next()
})

export default router
