<script setup>
import { onBeforeMount, ref } from 'vue'
import { useUsers } from '../stores/users.js'
import AppDropdown from '../components/App/Dropdown/AppDropdown.vue'
import AppDropdownItem from '../components/App/Dropdown/AppDropdownItem.vue'

const isLoading = ref(true)
const userStore = useUsers()

const confirmDeleteModal = ref(false)
const setConfirmDeleteModal = (state) => (confirmDeleteModal.value = state)

const viewDetailModal = ref(false)
const setViewDetailModal = (state) => (viewDetailModal.value = state)

const selectedUser = ref({})

const deleteUser = (user) => {
  selectedUser.value = user
  setConfirmDeleteModal(true)
}

const confirmDeleteUser = async () => {
  setConfirmDeleteModal(false)

  isLoading.value = true
  try {
    await userStore.deleteUserById(selectedUser.value.id)
  } catch (err) {
    console.log(err)
  } finally {
    isLoading.value = false
  }
}

const viewUserDetail = (user) => {
  selectedUser.value = user
  setViewDetailModal(true)
}

onBeforeMount(async () => {
  await userStore.fetchUsers()
  isLoading.value = false
})
</script>

<template>
  <app-modal
    :show="viewDetailModal"
    modal-type="info"
    @ok="setViewDetailModal(false)"
  >
    <h1>User Detail</h1>
    <h3>Name : </h3> {{ selectedUser.name }}
    <h3>Email : </h3> {{ selectedUser.email }}
    <h3>Role : </h3> {{ selectedUser.role }}
    <h3>Created On : </h3> {{ selectedUser.createdOn }}
    <h3>Updated On : </h3> {{ selectedUser.updatedOn }}
  </app-modal>
  <app-modal
    :show="confirmDeleteModal"
    modal-type="confirm"
    @confirm="confirmDeleteUser"
    @cancel="setConfirmDeleteModal(false)"
  >
    <h3 class="delete-modal-title">Are you sure to Delete?</h3>
    <h2>Delete User {{ selectedUser.name }}</h2>
  </app-modal>
  <app-loading-screen v-if="isLoading" />
  <div v-else id="users">
    <div class="user-list-header">
      <h4>
        {{ userStore.users.length || 0 }}
        User(s).
      </h4>
    </div>
    <div class="user-list">
      <div class="user-card" v-for="user in userStore.users" :key="user.userId" >
        <div class="user-card-content" >
          <div>
            <div class="user-card-detail" @click="viewUserDetail(user)">
              <div class="role-badge">{{ user.role.toUpperCase() }}</div>
              <b>{{ user.name }}</b>
            </div>
            <div class="user-card-detail" style="margin-top: 12px" @click="viewUserDetail(user)">
              <font-awesome-icon icon="envelope" />
              {{ user.email }}
            </div>
          </div>
          <AppDropdown>
            <AppDropdownItem>
              <font-awesome-icon icon="pen" />
              <span>Edit</span>
            </AppDropdownItem>
            <AppDropdownItem style="color: red; font-weight: bold" @click.prevent="deleteUser(user)">
              <font-awesome-icon icon="trash" />
              <span>Delete</span>
            </AppDropdownItem>
          </AppDropdown>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#users {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: rgba(245, 245, 245, 0.55);
}

.user-list-header {
  background-color: whitesmoke;
  border-bottom: 1px solid rgba(20, 20, 20, 0.1);
  padding: 12px;
}

.user-list {
  position: relative;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  overflow-y: scroll;
  padding: 8px 18px;
}

.user-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 30px 38px;
  border-radius: 10px;
  box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
}

.user-card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.user-card-detail {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 8px;
}

.user-card-detail:hover {
  color: #26e200;
  cursor: pointer;
}
.delete-modal-title {
  font-weight: 400;
  opacity: 0.5;
  padding-bottom: 12px;
}

.role-badge {
  background-color: #006494;
  border-radius: 20px;
  padding: 2px 10px;
  height: fit-content;
  font-size: 0.8em;
  color: white;
}

@media screen and (max-width: 1097px) {
  .user-list {
    grid-template-columns: repeat(1, 1fr);
  }
}
</style>
