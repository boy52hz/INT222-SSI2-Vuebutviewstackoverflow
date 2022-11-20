<script setup>
import { ref } from 'vue'

const isMenuOpened = ref(false)
const toggleMenu = () => {
  isMenuOpened.value = !isMenuOpened.value
}
</script>

<template>
  <nav class="bg-primary-blue drop-shadow p-3 lg:p-6 lg:py-8 lg:px-10 space-y-4">
    <div class="lg:hidden text-right" @click="toggleMenu">OPEN</div>
    <ul id="nav-list" :class="`space-y-2 ${isMenuOpened ? 'block' : 'hidden'} lg:block`">
      <li>
        <font-awesome-icon icon="fa-solid fa-calendar-alt" />
        <RouterLink to="/">Schedules</RouterLink>
      </li>
      <li>
        <font-awesome-icon icon="fa-solid fa-list" />
        <RouterLink to="/categories">Categories</RouterLink>
      </li>
      <li>
        <font-awesome-icon icon="fa-solid fa-users" />
        <RouterLink to="/users">Users</RouterLink>
      </li>
    </ul>
  </nav>
</template>

<style lang="scss" scoped>
ul#nav-list {
  li {
    @apply flex gap-3 justify-center lg:justify-start transition-colors duration-300 text-gray-300;

    svg {
      @apply w-5 h-5;
    }

    &:hover {
      @apply text-white;
    }
  }
}
</style>
