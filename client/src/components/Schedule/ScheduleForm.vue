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

const editableEvent = ref({})

const fetchCategories = async () => {
  const { data, error } = await categoriesApi.getCategories()
  categories.value = data
  editableEvent.value.category = data[0]
}

watchEffect(() => {
  editableEvent.value = {
    ...props.eventModel,
    eventStartTime: props.isEditMode ? moment(props.eventModel?.eventStartTime).format('YYYY-MM-DDThh:mm') : now,
  }

  if (!props.isEditMode) {
    fetchCategories()
  }
})

const selectCategory = (category) => {
  editableEvent.value.category = category
}

const attachFile = (file) => {
  editableEvent.value.file = file
}

const submitCreateEventForm = (evt) => {
  emit('save-event', editableEvent.value)
}
</script>

<template>
  <form
    id="create-event-form"
    class="container lg:max-w-xl mx-auto space-y-2 text-sm"
    @submit.prevent="submitCreateEventForm"
  >
    <div>
      <label class="required">Booking name</label>
      <AppInput
        v-model="editableEvent.bookingName"
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
        v-model="editableEvent.bookingEmail"
        type="email"
        :required="true"
        :minlength="1"
        :maxlength="100"
        :disabled="authStore.isAuthenticated"
      />
    </div>
    <div>
      <label class="required">Start time</label>
      <AppInput v-model="editableEvent.eventStartTime" type="datetime-local" :required="true" :min="now" />
    </div>
    <div class="space-y-2">
      <label class="required">Select category</label>
      <span v-if="isEditMode">
        <ScheduleCategoryBadge v-if="isEditMode" class="drop-shadow-md" :category="editableEvent.category" />
      </span>
      <span v-else>
        <div class="flex flex-wrap gap-3" v-if="categories.length > 0">
          <ScheduleCategoryBadge
            v-for="category in categories"
            :class="`opacity-60 scale-60 text-lg drop-shadow-sm hover:opacity-100 hover:cursor-pointer transition-all duration-300 ${
              category.categoryId === editableEvent.category?.categoryId && '!opacity-100 scale-110 drop-shadow-md'
            }`"
            :category="category"
            @click="selectCategory(category)"
          />
        </div>
        <div v-else class="text-gray-500 italic">Loading categories...</div>
      </span>
    </div>
    <div>
      <label>Note</label>
      <AppTextField v-model="editableEvent.eventNotes" :maxlength="500" :resize="false" :rows="4" />
    </div>
    <AppFileInput :file-model="editableEvent.file" @input="attachFile" />
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
