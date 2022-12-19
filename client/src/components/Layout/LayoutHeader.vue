<script setup>
import { ref } from 'vue'
import { useAuthenticationStore } from '../../stores/authentication'
import AppModal from '../App/AppModal.vue'
import AuthenticationForm from '../Authentication/AuthenticationForm.vue'
import { useToast } from 'vue-toastification'
import { useRouter } from 'vue-router'

const router = useRouter()
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
  toast.success('You are now Logged in')
  await authStore.retrieveUser()
  closeAuthenticationPopup()
}

const loginWithMs = async () => {
  const { error } = await authStore.loginWithMs()
  if (error) {
    toast.error('Failed to sign-in with microsoft account.')
    return
  }
  toast.success('You are now Logged in')
  console.log(authStore.user)
  closeAuthenticationPopup()
}

const registerUser = async ({ name, email, password, roleName }) => {
  const { data, error } = await authStore.registerUser({ name, email, password, roleName })
  if (error) {
    toast.error(error)
    return
  }
  toast.success('Your account has been created.')
  closeAuthenticationPopup()
}

const logout = async () => {
  localStorage.removeItem('authenticationType')
  if (authStore.authenticationType === 'local') {
    authStore.logout()
  } else if (authStore.authenticationType === 'msal') {
    authStore.logoutMs()
  }
  toast.success('You are now logged out.')
  router.replace('/')
}
</script>

<template>
  <AppModal :show="showLoginModal" @cancel="closeAuthenticationPopup">
    <AuthenticationForm @login="loginUser" @login-ms="loginWithMs" @register="registerUser" />
  </AppModal>
  <div class="px-5 py-3 bg-light drop-shadow-sm w-full flex justify-end">
    <div
      class="underline transition-all duration-300 hover:text-sky-500 hover:cursor-pointer"
      v-if="!authStore.isAuthenticated"
      @click="openAuthenticationPopup"
    >
      Login
    </div>
    <div v-else class="text-right leading-tight grid grid-cols-2 gap-5">
      <div class="text-xs text-red-500 hover:text-red-600 hover:cursor-pointer h-fit" @click.prevent="logout">
        Logout
      </div>
      <div>
        <div>{{ authStore.user?.name }}</div>
        <small>{{ authStore.user?.role }}</small>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
