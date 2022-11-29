<script setup>
import { computed, readonly, ref, watch } from 'vue'
import AppButton from '../App/AppButton.vue'
import AppInput from '../App/AppInput.vue'
import * as rolesApi from '../../apis/roles'
import AppSelectOptions from '../App/AppSelectOptions.vue'

const authenticationFormTemplate = readonly({
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
  roleName: '',
})

const emit = defineEmits(['login', 'login-ms', 'register'])

const isLoginMode = ref(true)
const roles = ref([])

const authenticationForm = ref({ ...authenticationFormTemplate })
const authenticationFormError = ref({ ...authenticationFormTemplate })

const validatePwdMatchOnUnfocus = () => {
  if (isLoginMode.value) return

  authenticationFormError.value.confirmPassword = ''

  const { password, confirmPassword } = authenticationForm.value
  if ([password, confirmPassword].some((value) => value === '')) return

  if (password !== confirmPassword) {
    authenticationFormError.value.confirmPassword = 'Password and Confirm password are not matched.'
  }
}

const onSubmitForm = () => {
  const { name, email, password, roleName } = authenticationForm.value
  if (isLoginMode.value) {
    emit('login', { email, password })
  } else {
    emit('register', { name, email, password, roleName })
  }
}

const mappedRoleToSelectOptions = computed(() => {
  return roles.value?.map((role) => ({ label: role.label, value: role.name }))
})

watch(
  () => isLoginMode.value,
  async () => {
    if (isLoginMode.value) return
    if (roles.value.length > 0) return

    const { data, error } = await rolesApi.getRegisterableRoles()
    if (error) {
      console.log(error)
      return
    }
    roles.value = data
    authenticationForm.value.roleName = data[0].name
  }
)
</script>

<template>
  <form class="space-y-3" @submit.prevent="onSubmitForm">
    <h2 class="text-2xl text-center">{{ isLoginMode ? 'Login' : 'Register' }}</h2>
    <div v-if="!isLoginMode">
      <label for="name" class="required">Name</label>
      <AppInput v-model="authenticationForm.name" id="name" type="text" :maxlength="100" :required="true" />
    </div>

    <div>
      <label for="email" class="required">Email</label>
      <AppInput
        v-model.trim="authenticationForm.email"
        id="email"
        type="email"
        pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
        :maxlength="50"
        :required="true"
      />
    </div>
    <div>
      <label for="password" class="required">Password</label>
      <AppInput
        v-model="authenticationForm.password"
        id="password"
        type="password"
        :error-message="authenticationFormError.password"
        :minlength="8"
        :maxlength="14"
        :required="true"
        @focusout="validatePwdMatchOnUnfocus"
      />
    </div>

    <template v-if="!isLoginMode">
      <div>
        <label for="confirm-password" class="required">Confirm password</label>
        <AppInput
          v-model="authenticationForm.confirmPassword"
          id="confirm-password"
          type="password"
          :error-message="authenticationFormError.confirmPassword"
          :minlength="8"
          :maxlength="14"
          :required="true"
          @focusout="validatePwdMatchOnUnfocus"
        />
      </div>
      <div>
        <label for="role" class="required">Role</label>
        <AppSelectOptions v-model="authenticationForm.roleName" :options="mappedRoleToSelectOptions" />
      </div>
    </template>

    <div class="space-y-3" v-if="isLoginMode">
      <AppButton variant="primary" type="submit">Login</AppButton>
      <AppButton
        class="flex justify-center items-center gap-3"
        variant="light"
        type="button"
        @click="$emit('login-ms')"
      >
        <font-awesome-icon icon="fa-brands fa-microsoft" /><span>Sign-in with Microsoft</span>
      </AppButton>
      <hr class="divide-x bg-slate-300" />
      <button class="block mx-auto text-sm" type="button" @click="isLoginMode = false">Create an account</button>
    </div>
    <div class="space-y-3" v-else>
      <AppButton variant="primary" type="submit">Register</AppButton>
      <button class="block mx-auto text-sm" type="button" @click="isLoginMode = true">I already have an account</button>
    </div>
  </form>
</template>

<style scoped></style>
