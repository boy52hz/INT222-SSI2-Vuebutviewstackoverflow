<script setup>
import moment from 'moment'
import { ref, computed, watchEffect, watch } from 'vue'
import AppInput from '../App/AppInput.vue'
import * as categoriesApi from '@/apis/categories'
import ScheduleCategoryBadge from './ScheduleCategoryBadge.vue'
import AppTextField from '../App/AppTextField.vue'
import AppFileInput from '../App/AppFileInput.vue'
import AppButton from '../App/AppButton.vue'
import { useAuthenticationStore } from '@/stores/authentication'

const authStore = useAuthenticationStore()
const now = moment().format('YYYY-MM-DDThh:mm')

const categories = ref([])

const props = defineProps({
  eventModel: {
    type: Object,
    default: {},
  },
  isEditMode: {
    type: Boolean,
    default: false,
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['save-event'])

const eventModel = ref({})

watchEffect(() => {
  eventModel.value = { ...props.eventModel, eventStartTime: now }
})

const fetchCategories = async () => {
  const { data, error } = await categoriesApi.getCategories()
  categories.value = data
  eventModel.value.category = data[0]
}

const selectCategory = (category) => {
  eventModel.value.category = category
}

const attachFile = (file) => {
  eventModel.value.file = file
}

const submitCreateEventForm = (evt) => {
  emit('save-event', eventModel.value)
}

// const resetForm = () => {
//   eventModel.value = { ...props.eventModel, eventStartTime: now }
//   eventModel.value.category = categories.value[0]
// }

fetchCategories()
</script>

<template>
  <form
    id="create-event-form"
    class="container lg:max-w-xl mx-auto space-y-3 text-sm"
    @submit.prevent="submitCreateEventForm"
  >
    <div>
      <label class="required">Booking name</label>
      <AppInput
        v-model="eventModel.bookingName"
        type="text"
        :minlength="1"
        :maxlength="100"
        :required="true"
        :disabled="isEditMode"
      />
    </div>
    <div>
      <label class="required">Booking email</label>
      <AppInput
        v-model="eventModel.bookingEmail"
        type="email"
        :required="true"
        :minlength="1"
        :maxlength="100"
        :disabled="authStore.isAuthenticated"
      />
    </div>
    <div>
      <label class="required">Start time</label>
      <AppInput v-model="eventModel.eventStartTime" type="datetime-local" :required="true" :min="now" />
    </div>
    <div class="space-y-2">
      <label class="required">Select category</label>
      <div class="flex flex-wrap gap-3" v-if="categories.length > 0">
        <ScheduleCategoryBadge v-if="isEditMode" class="drop-shadow-md" :category="eventModel?.category" />
        <ScheduleCategoryBadge
          v-else
          v-for="category in categories"
          :class="`opacity-60 scale-60 text-lg drop-shadow-sm hover:opacity-100 hover:cursor-pointer transition-all duration-300 ${
            category.categoryId === eventModel.category?.categoryId && '!opacity-100 scale-110 drop-shadow-md'
          }`"
          :category="category"
          @click="selectCategory(category)"
        />
      </div>
      <div v-else class="text-gray-500 italic">Loading categories...</div>
    </div>
    <div>
      <label>Note</label>
      <AppTextField v-model="eventModel.eventNotes" :maxlength="500" :resize="false" :rows="4" />
    </div>
    <AppFileInput @input="attachFile" />
    <div class="mt-5">
      <AppButton type="submit" :is-loading="isLoading">Save</AppButton>
    </div>
  </form>
</template>

<style lang="scss" scoped>
#create-event-form {
  label {
    @apply mb-2;
  }
}
</style>
