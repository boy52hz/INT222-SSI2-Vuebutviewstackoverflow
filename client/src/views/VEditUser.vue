<script setup>
import PageWrapper from '../components/PageWrapper.vue'
import AppInput from '../components/App/AppInput.vue'
import { useUsers } from '../stores/users'
import { onBeforeMount, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const userStore = useUsers()

const roles = reactive(['student', 'lecturer', 'admin'])

const formEdit = ref({
  fistName: '',
  lastName: '',
  email: '',
})

const errorMessage = ref({
  firstName: '',
  lastName: '',
  email: '',
})

onBeforeMount(async () => {
  const { userId } = route.params
  try {
    const user = await userStore.fetchUserByUserId(userId)
    const [firstName, lastName] = user.name.split(' ')
    formEdit.value = {
      firstName,
      lastName,
      email: user.email,
      role: user.role,
    }
  } catch (err) {
    console.log(err)
  }
})
</script>

<template>
  <PageWrapper>
    <div id="edit-user">
      <form @submit.prevent="">
        <div class="inline-input-group">
          <AppInput
            type="text"
            v-model="formEdit.firstName"
            label-text="First Name"
            :error-message="errorMessage.firstName"
            :required="true"
          />
          <AppInput
            type="text"
            v-model="formEdit.lastName"
            label-text="Last Name"
            :error-message="errorMessage.lastName"
            :required="true"
          />
        </div>
        <AppInput
          type="email"
          v-model="formEdit.email"
          label-text="Email"
          :error-message="errorMessage.email"
          :required="true"
        />
        <div class="input-group">
          <label class="required">Role</label>
          <select v-model="formEdit.role" style="width: 100%">
            <option v-for="(role, index) in roles" :key="index" :value="role">{{ $capitalize(role) }}</option>
          </select>
        </div>
      </form>
    </div>
  </PageWrapper>
</template>

<style scoped>
#edit-user {
  max-width: 550px;
  width: 100%;
  margin: 0 auto;
  height: 100%;
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
