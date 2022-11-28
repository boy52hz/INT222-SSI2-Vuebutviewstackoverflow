<script setup>
import { RouterView } from 'vue-router'
import LayoutMain from './components/Layout/LayoutMain.vue'
import { useAuthenticationStore } from './stores/authentication'

const authStore = useAuthenticationStore()
const { accessToken } = authStore.getToken()
authStore.setToken(accessToken)
</script>

<template>
  <LayoutMain>
    <RouterView />
  </LayoutMain>
</template>

<style scoped></style>
