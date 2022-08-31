<script setup>
import PageWrapper from '../components/PageWrapper.vue'
import AppInput from '../components/App/AppInput.vue'
import { useUsers } from '../stores/users'
import { computed, onBeforeMount, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useUserRoles } from '../stores/userRoles'
import router from '../router'

const route = useRoute()
const userStore = useUsers()
const roleStore = useUserRoles()

const userData = ref({})
const roleOptions = ref([])
const isLoading = ref(false)

const formEdit = ref({
  name: '',
  email: '',
})

const errorMessage = ref({
  name: '',
  email: '',
})

const isEdited = computed(() => {
  return (
    formEdit.value.email !== userData.value.email ||
    formEdit.value.name !== userData.value.name ||
    formEdit.value.role !== userData.value.role.name
  )
})

const editUser = async () => {
  try {
    const success = await userStore.editUser(userData.value.id, formEdit.value)
    if (success) {
      alert('Your data has been edited!')
      router.push('/users')
    }
  } catch (err) {
    const fieldErrors = err.fieldErrors
    for (let key in fieldErrors) {
      errorMessage.value[key] = fieldErrors[key].join(', ')
    }
  }
}

onBeforeMount(async () => {
  const { userId } = route.params

  isLoading.value = true
  try {
    const user = await userStore.fetchUserByUserId(userId)
    userData.value = user
    formEdit.value = {
      name: user.name,
      email: user.email,
      role: user.role.name,
    }
    await roleStore.fetchUserRoles()
    roleOptions.value = roleStore.toOptions()
    isLoading.value = false
  } catch (err) {
    console.log(err)
  }
})
</script>

<template>
  <PageWrapper>
    <app-loading-screen v-show="isLoading" />
    <div id="edit-user">
      <div class="container">
        <h2>Edit user - {{ userData.name }}</h2>
        <form @submit.prevent="editUser">
          <div class="inline-input-group">
            <AppInput
              type="text"
              v-model.trim="formEdit.name"
              label-text="Name"
              :maxlenth="100"
              :error-message="errorMessage.name"
              :required="true"
            />
          </div>
          <AppInput
            type="email"
            v-model.trim="formEdit.email"
            label-text="Email"
            :maxlength="50"
            :error-message="errorMessage.email"
            :required="true"
          />
          <AppInput
            v-model.trim="formEdit.role"
            label-text="Role"
            :options="roleOptions"
            :error-message="errorMessage.role"
            :required="true"
          />
          <div class="app-button-group">
            <app-button type="submit" button-type="warning" :disabled="!isEdited">Save</app-button>
            <app-button type="reset" button-type="danger" @click.prevent="$router.back()">Cancel</app-button>
          </div>
        </form>
      </div>
    </div>
  </PageWrapper>
</template>

<style scoped>
#edit-user {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.container {
  max-width: 550px;
  width: 100%;
  margin: 0 auto;
}

.inline-input-group {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.inline-input-group div {
  width: 100%;
}
</style>
