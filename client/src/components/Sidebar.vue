<script setup>
import { ref } from 'vue'
import { Role } from '../enums/Role'
import { useAuthenticationStore } from '../stores/authentication'
const authStore = useAuthenticationStore()
const isMenuOpened = ref(false)
const toggleMenu = () => {
  isMenuOpened.value = !isMenuOpened.value
}
</script>

<template>
  <nav class="bg-primary-blue drop-shadow p-0 lg:py-8 lg:px-4 space-y-4">
    <!-- <div class="lg:hidden text-right" @click="toggleMenu">OPEN</div> -->
    <ul class="nav-list" :class="`space-y-2 ${isMenuOpened ? 'block' : 'hidden'} lg:block`">
      <li>
        <RouterLink to="/">
          <font-awesome-icon icon="fa-solid fa-calendar-alt" />
          <span>Schedules</span>
        </RouterLink>
      </li>
      <li>
        <ul class="nav-list nested">
          <li>
            <RouterLink to="/create-event">
              <font-awesome-icon icon="fa-solid fa-plus" />
              <span>Create New Event</span>
            </RouterLink>
          </li>
        </ul>
      </li>
      <li v-if="[Role.Admin, Role.Lecturer, Role.Student].includes(authStore.user?.role)">
        <RouterLink to="/categories">
          <font-awesome-icon icon="fa-solid fa-list" />
          <span>Categories</span>
        </RouterLink>
      </li>
      <li v-if="[Role.Admin].includes(authStore.user?.role)">
        <RouterLink to="/users">
          <font-awesome-icon icon="fa-solid fa-users" />
          <span>Users</span>
        </RouterLink>
      </li>
      <li v-if="[Role.Admin].includes(authStore.user?.role)">
        <ul class="nav-list nested">
          <li>
            <RouterLink to="/create-user">
              <font-awesome-icon icon="fa-solid fa-plus" />
              <span>Create New User</span>
            </RouterLink>
          </li>
        </ul>
      </li>
    </ul>
  </nav>
</template>

<style lang="scss" scoped>
ul.nav-list {
  li {
    a {
      @apply flex gap-3 justify-center lg:justify-start transition-colors duration-300 text-gray-300 opacity-70;

      svg {
        @apply w-5 h-5;
      }

      &.router-link-active,
      &.router-linl-exact-active {
        @apply text-white opacity-100;
      }

      &:hover {
        @apply text-white;
      }
    }

    ul li {
      @apply ml-4;
    }
  }

  &.nested {
    @apply text-sm;
  }
}
</style>
