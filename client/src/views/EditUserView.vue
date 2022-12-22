<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import * as usersApi from '../apis/users'
import UserEditForm from '../components/User/UserEditForm.vue'

const toast = useToast()
const route = useRoute()
const router = useRouter()

const isFetching = ref(true)
const isLoading = ref(false)

const editUserForm = ref({
  id: '',
  name: '',
  email: '',
  roleName: '',
})

const handleSaveUser = async ({ id, name, email, roleName }) => {
  isLoading.value = true
  const { data, error } = await usersApi.editUser(id, { name, email, roleName })
  if (error) {
    isLoading.value = false
    toast.error(Object.values(error?.fieldErrors).join(', ') || error?.message)
    return
  }
  toast.success('Saved')
  router.replace('/users')
}

const fetchUserById = async (userId) => {
  isFetching.value = true
  const { data: user, error } = await usersApi.getUserById(userId)
  isFetching.value = false
  if (error) {
    router.replace('/')
    return
  }
  editUserForm.value = {
    id: user.id,
    name: user.name,
    email: user.email,
    roleName: user.role.name,
  }
}

watch(
  () => route.params,
  () => {
    fetchUserById(route.params.userId)
  },
  { immediate: true }
)
</script>

<template>
  <div class="container max-w-lg mx-auto space-y-3">
    <div class="font-bold text-xl">Edit User - {{ editUserForm?.name }}</div>
    <UserEditForm :user-model="editUserForm" @save="handleSaveUser" :is-loading="isLoading" />
  </div>
</template>

<style scoped></style>
