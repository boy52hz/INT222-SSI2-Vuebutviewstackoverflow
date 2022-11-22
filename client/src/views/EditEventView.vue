<script setup>
import moment from 'moment'
import { ref } from 'vue'
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
  const { data, error } = await eventsApi.editEvent(bookingId, {
    bookingName,
    categoryId: category.categoryId,
    eventStartTime: moment(eventStartTime).toISOString(),
    eventNotes,
    file,
  })
  isLoading.value = false
  if (error) {
    console.log(error)
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

fetchEventById(parseInt(route.params?.bookingId))
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
