<script setup>
import { ref, watchEffect, computed } from 'vue'
import { formUtils } from '../../utils/form'
import AppInput from '../App/AppInput.vue'
import AppSelectOptions from '../App/AppSelectOptions.vue'
import * as rolesApi from '../../apis/roles'
import AppButton from '../App/AppButton.vue'

const roles = ref([])
const editableUser = ref({})
const errorMessage = ref({
  name: '',
  email: '',
})

defineEmits(['save'])

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
  editableUser.value = {
    id: props.userModel.id,
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
  return !formUtils.isEditableDataChanged(props.userModel, editableUser.value)
})

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
  <form class="space-y-5" v-if="editableUser" @submit.prevent="$emit('save', editableUser)">
    <div>
      <label class="required">Name</label>
      <AppInput
        v-model="editableUser.name"
        name="name"
        type="text"
        @focusout="checkValidation"
        :error-message="errorMessage.name"
        :maxlength="100"
        :required="true"
      />
    </div>
    <div>
      <label class="required">Email</label>
      <AppInput
        v-model="editableUser.email"
        name="email"
        type="email"
        pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
        @focusout="checkValidation"
        :error-message="errorMessage.email"
        :maxlength="50"
        :required="true"
      />
    </div>
    <div>
      <label class="required">Role</label>
      <AppSelectOptions v-if="editableUser.roleName" v-model="editableUser.roleName" :options="roles" />
    </div>
    <AppButton type="submit" :disabled="shouldDisableSaveButton" :is-loading="isLoading">Save</AppButton>
  </form>
</template>

<style scoped></style>
