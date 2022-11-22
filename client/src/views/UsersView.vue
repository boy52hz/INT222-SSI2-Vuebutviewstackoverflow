<script setup>
import { ref } from 'vue'
import * as usersApi from '../apis/users'
import UserDetail from '../components/User/UserDetail.vue'
import AppModal from '../components/App/AppModal.vue'
import AppTable from '../components/App/AppTable.vue'
import AppButton from '../components/App/AppButton.vue'
import { useToast } from 'vue-toastification'

const toast = useToast()
const users = ref([])

const isLoading = ref(true)
const userModal = ref(false)
const deleteConfirmModal = ref(false)
const userDetail = ref({})

const showUserModal = (user) => {
  userModal.value = true
  userDetail.value = user
}
const closeUserModal = () => {
  userModal.value = false
  userDetail.value = {}
}
const showDeleteConformModal = (user) => {
  deleteConfirmModal.value = true
  userDetail.value = user
}
const closeDeleteConformModal = () => {
  deleteConfirmModal.value = false
  userDetail.value = {}
}

const deleteUser = async (user) => {
  closeDeleteConformModal()
  isLoading.value = true
  const { data, error } = await usersApi.deleteUser(user.id)
  if (error) {
    toast.error(error.message)
    return
  }

  await fetchUsers()
  toast.success(data?.message || `Deleted user "${user.name}"`)
}

const fetchUsers = async () => {
  isLoading.value = true
  const { data, error } = await usersApi.getUsers()
  if (error) {
    console.log(error)
    return
  }
  users.value = data
  isLoading.value = false
}

fetchUsers()
</script>

<template>
  <div class="container max-w-3xl mx-auto h-full overflow-hidden flex flex-col space-y-2">
    <div class="text-sm">{{ users?.length || 0 }} Users found</div>
    <AppTable class="text-left">
      <template #head>
        <th scope="col" class="user-table-col">Name</th>
        <th scope="col" class="user-table-col">Email</th>
        <th scope="col" colspan="2" class="user-table-col">Role</th>
      </template>
      <template v-if="!isLoading" #body>
        <tr
          v-for="user in users"
          :key="user.id"
          class="bg-white border-b hover:bg-light hover:cursor-pointer"
          @click="showUserModal(user)"
        >
          <td class="user-table-col text-gray-900 whitespace-nowrap">{{ user.name }}</td>
          <td class="user-table-col">{{ user.email }}</td>
          <td class="user-table-col">{{ user.role.label }}</td>
          <td class="user-table-col grid grid-cols-1 md:grid-cols-2 gap-3 text-base">
            <div class="text-amber-500 hover:text-amber-700 transition-all duration-300">
              <font-awesome-icon icon="fa-regular fa-pen-to-square" />
            </div>
            <div
              class="text-red-500 hover:text-red-700 transition-all duration-300"
              @click.stop="showDeleteConformModal(user)"
            >
              <font-awesome-icon icon="fa-solid fa-trash-alt" />
            </div>
          </td>
        </tr>
      </template>
      <template v-else #body>
        <div class="p-5">Loading users...</div>
      </template>
    </AppTable>
  </div>
  <AppModal :show="userModal" @cancel="closeUserModal">
    <UserDetail :user="userDetail" />
  </AppModal>
  <AppModal :show="deleteConfirmModal" @cancel="closeDeleteConformModal">
    <div class="space-y-3">
      <h2 class="text-gray-500">Are you sure to delete this user?</h2>
      <div class="text-center text-xl">#{{ userDetail?.id }} {{ userDetail?.name }}</div>
      <div class="flex gap-2">
        <AppButton variant="danger" @click="deleteUser(userDetail)" :is-loading="isLoading">Yes</AppButton>
        <AppButton variant="light" @click="closeDeleteConformModal">No</AppButton>
      </div>
    </div>
  </AppModal>
</template>

<style lang="scss" scoped>
.user-table-col {
  @apply py-3 px-6;
}
</style>
