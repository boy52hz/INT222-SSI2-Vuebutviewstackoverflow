<script setup>
import { ref, watchEffect, computed } from 'vue'
import AppInput from '../App/AppInput.vue'
import AppSelectOptions from '../App/AppSelectOptions.vue'
import * as rolesApi from '../../apis/roles'
import AppButton from '../App/AppButton.vue'

const roles = ref([])
const editableUser = ref({})

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

const shouldDisableSaveButton = computed(() => {
  if (props.isLoading) return true

  let disabled = true
  for (let [key, value] of Object.entries(editableUser.value)) {
    if (value !== props.userModel[key]) {
      disabled = false
    }
  }
  return disabled
})

const fetchRegisterableRoles = async () => {
  const { data, error } = await rolesApi.getRegisterableRoles()
  if (error) {
    console.log(error)
    return
  }

  roles.value = data.map((role) => ({ label: role.label, value: role.name }))
}

fetchRegisterableRoles()
</script>

<template>
  <form class="space-y-5" v-if="editableUser" @submit.prevent="$emit('save', editableUser)">
    <div>
      <label class="required">Name</label>
      <AppInput v-model="editableUser.name" type="text" :required="true" />
    </div>
    <div>
      <label class="required">Email</label>
      <AppInput v-model="editableUser.email" type="email" :required="true" />
    </div>
    <div>
      <label class="required">Role</label>
      <AppSelectOptions v-if="editableUser.roleName" v-model="editableUser.roleName" :options="roles" />
    </div>
    <AppButton type="submit" :disabled="shouldDisableSaveButton" :is-loading="isLoading">Save</AppButton>
  </form>
</template>

<style scoped></style>
