<script setup>
import moment from 'moment'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import * as eventsApi from '../apis/events'
import ScheduleForm from '../components/Schedule/ScheduleForm.vue'

const toast = useToast()
const route = useRoute()
const router = useRouter()

const event = ref(null)
const isLoading = ref(false)
const isFetching = ref(true)

const saveEvent = async ({ bookingId, bookingName, category, eventStartTime, eventNotes, file }) => {
  isLoading.value = true

  // Check if incoming file is instance of File class. If not, create empty file object with exist file metadata
  let fileToUpdate = null
  if (file !== null) {
    fileToUpdate = file instanceof File ? file : new File([], file.name, { type: file.type })
  }

  const { error } = await eventsApi.editEvent(bookingId, {
    bookingName,
    categoryId: category.categoryId,
    eventStartTime: moment(eventStartTime).toISOString(),
    eventNotes,
    file: fileToUpdate,
  })
  isLoading.value = false
  if (error) {
    toast.error(Object.values(error?.fieldErrors).join('\n') || error?.message)
    return
  }
  await router.push({ path: '/', query: { bookingId } })
  toast.success('Event saved')
}

const fetchEventById = async (bookingId) => {
  isFetching.value = true
  const { data, error } = await eventsApi.getEventById(bookingId)
  isFetching.value = false
  if (error) {
    router.replace('/')
    return
  }
  event.value = data
}

watch(
  () => route.params,
  () => {
    fetchEventById(parseInt(route.params?.bookingId))
  },
  { immediate: true }
)
</script>

<template>
  <div class="container max-w-lg mx-auto space-y-3">
    <div class="font-bold text-xl">Edit event - {{ event?.bookingName }}</div>
    <ScheduleForm
      v-show="!isFetching"
      :event-model="event"
      :is-edit-mode="true"
      @save-event="saveEvent"
      :is-loading="isLoading"
    />
  </div>
</template>

<style scoped></style>
