<script setup>
import { ref, watchEffect, computed } from 'vue'
import { formUtils } from '../../utils/form'
import AppInput from '../App/AppInput.vue'
import AppSelectOptions from '../App/AppSelectOptions.vue'
import * as rolesApi from '../../apis/roles'
import AppButton from '../App/AppButton.vue'

const roles = ref([])
const newUserModel = ref({})
const errorMessage = ref({
  name: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const emit = defineEmits(['save'])

const props = defineProps({
  userModel: {
    type: Object,
    default: {},
  },
  isLoading: {
    type: Boolean,
    default: true,
  },
})

watchEffect(() => {
  newUserModel.value = {
    name: props.userModel.name,
    email: props.userModel.email,
    roleName: props.userModel.roleName,
  }
})

const checkValidation = (evt) => {
  if (!evt.target.checkValidity()) {
    errorMessage.value[evt.target.name] = evt.target.validationMessage
    return
  }
  errorMessage.value[evt.target.name] = ''
}

const shouldDisableSaveButton = computed(() => {
  if (props.isLoading) return true
  return !formUtils.isEditableDataChanged(props.userModel, newUserModel.value)
})

const validatePasswordConfirmation = () => {
  errorMessage.value.password = ''
  errorMessage.value.confirmPassword = ''

  if (!newUserModel.value.password || !newUserModel.value.confirmPassword) {
    return false
  }
  if (newUserModel.value.password.length < 8 || newUserModel.value.password.length > 14) {
    errorMessage.value.password = 'Password size must be between 8 and 14.'
    return false
  }
  if (newUserModel.value.password !== newUserModel.value.confirmPassword) {
    errorMessage.value.confirmPassword = 'Password mismatch.'
    return false
  } else {
    errorMessage.value.confirmPassword = ''
    return true
  }
}

const fetchRoles = async () => {
  const { data, error } = await rolesApi.getRoles()
  if (error) {
    console.log(error)
    return
  }

  roles.value = data.map((role) => ({ label: role.label, value: role.name }))
}

fetchRoles()
</script>

<template>
  <form class="space-y-5" v-if="newUserModel" @submit.prevent="$emit('save', newUserModel)">
    <div>
      <label class="required">Name</label>
      <AppInput v-model="newUserModel.name" name="name" type="text" :maxlength="100" :required="true" />
    </div>
    <div>
      <label class="required">Email</label>
      <AppInput
        v-model="newUserModel.email"
        pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
        name="email"
        type="email"
        @focusout="checkValidation"
        :error-message="errorMessage.email"
        :maxlength="50"
        :required="true"
      />
    </div>
    <div>
      <label class="required">Password</label>
      <AppInput
        v-model="newUserModel.password"
        name="password"
        type="password"
        @focusout="validatePasswordConfirmation"
        :error-message="errorMessage.password"
        :minlength="8"
        :maxlength="14"
        :required="true"
      />
    </div>
    <div>
      <label class="required">Confirm Password</label>
      <AppInput
        v-model="newUserModel.confirmPassword"
        name="confirmPassword"
        type="password"
        @focusout="validatePasswordConfirmation"
        :error-message="errorMessage.confirmPassword"
        :minlength="8"
        :maxlength="14"
        :required="true"
      />
    </div>
    <div>
      <label class="required">Role</label>
      <AppSelectOptions v-model="newUserModel.roleName" :options="roles" />
    </div>
    <AppButton type="submit" :disabled="shouldDisableSaveButton" :is-loading="isLoading">Save</AppButton>
  </form>
</template>

<style scoped></style>
