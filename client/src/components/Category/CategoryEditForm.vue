<script setup>
import { ref, watchEffect, computed } from 'vue'
import { formUtils } from '../../utils/form'
import AppButton from '../App/AppButton.vue'
import AppInput from '../App/AppInput.vue'
import AppTextField from '../App/AppTextField.vue'

defineEmits(['save'])

const props = defineProps({
  categoryModel: {
    type: Object,
    default: {},
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
})

const editableCategory = ref({})

const shouldDisableSaveButton = computed(() => {
  if (props.isLoading) return true
  return !formUtils.isEditableDataChanged(props.categoryModel, editableCategory.value)
})

watchEffect(() => {
  editableCategory.value = {
    categoryId: props.categoryModel.categoryId,
    categoryName: props.categoryModel.categoryName,
    eventDuration: props.categoryModel.eventDuration,
    eventCategoryDesc: props.categoryModel.eventCategoryDesc,
  }
})
</script>

<template>
  <form class="space-y-3" @submit.prevent="$emit('save', editableCategory)">
    <div>
      <label class="required">Category name</label>
      <AppInput v-model="editableCategory.categoryName" :max-length="100" type="text" :required="true" />
    </div>
    <div>
      <label class="required">Duration <small class="text-gray-500 italic">(1 - 480 min)</small></label>
      <AppInput v-model="editableCategory.eventDuration" :min="1" :max="480" type="number" :required="true" />
    </div>
    <div>
      <label>Description</label>
      <AppTextField :rows="5" v-model="editableCategory.eventCategoryDesc" :max-length="500" type="text" />
    </div>
    <AppButton type="submit" :is-loading="isLoading" :disabled="shouldDisableSaveButton">Save</AppButton>
  </form>
</template>

<style></style>
