<script setup>
import { ref } from 'vue'
import { useToast } from 'vue-toastification'
import UserCreateForm from '../components/User/UserCreateForm.vue'
import * as usersApi from '../apis/users'
import { useRouter } from 'vue-router'

const toast = useToast()
const router = useRouter()
const isLoading = ref(false)

const createUserModelTemplate = {
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
  roleName: '',
}

const createUserModel = ref({ ...createUserModelTemplate })

const createUser = async (user) => {
  isLoading.value = true
  const { data, error } = await usersApi.createUser(user)
  isLoading.value = false
  if (error) {
    toast.error(Object.values(error?.fieldErrors).join('\n') || error?.message)
    return
  }
  toast.success('Saved')
  createUserModel.value = { ...createUserModelTemplate }
  router.push('/users')
}
</script>

<template>
  <div class="container max-w-lg mx-auto space-y-3">
    <div class="font-bold text-xl">Create new user</div>
    <UserCreateForm :user-model="createUserModel" @save="createUser" :is-loading="isLoading" />
  </div>
</template>

<style></style>
