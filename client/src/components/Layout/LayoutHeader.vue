<script setup>
import { ref } from 'vue'
import { useAuthenticationStore } from '@/stores/authentication'
import AppModal from '../App/AppModal.vue'
import AuthenticationForm from '../Authentication/AuthenticationForm.vue'
import { useToast } from 'vue-toastification'
const toast = useToast()
const authStore = useAuthenticationStore()
const showLoginModal = ref(false)
const openAuthenticationPopup = () => {
  showLoginModal.value = true
}
const closeAuthenticationPopup = () => {
  showLoginModal.value = false
}
const loginUser = async ({ email, password }) => {
  const { data, error } = await authStore.login({ email, password })
  if (error) {
    console.log(error)
    toast.error(error.message)
    return
  }
  toast.success(data.message)
  await authStore.retrieveUser()
  closeAuthenticationPopup()
}
const loginWithMs = async () => {
  await authStore.loginWithMs()
  closeAuthenticationPopup()
}
</script>

<template>
  <AppModal :show="showLoginModal" @cancel="closeAuthenticationPopup">
    <AuthenticationForm @login="loginUser" @login-ms="loginWithMs" />
  </AppModal>
  <div class="px-5 py-3 bg-light drop-shadow-sm w-full flex justify-end">
    <div
      class="underline transition-all duration-300 hover:text-sky-500 hover:cursor-pointer"
      v-if="!authStore.isAuthenticated"
      @click="openAuthenticationPopup"
    >
      Login
    </div>
    <div v-else class="text-right leading-tight">
      <div>{{ authStore.user?.name }}</div>
      <small>{{ authStore.user?.role }}</small>
    </div>
  </div>
</template>

<style scoped></style>
