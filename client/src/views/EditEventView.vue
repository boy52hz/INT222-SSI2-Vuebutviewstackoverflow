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

const saveEvent = async ({ bookingId, bookingName, category, eventStartTime, eventNotes, file }) => {
  const { data, error } = await eventsApi.editEvent(bookingId, {
    bookingName,
    categoryId: category.categoryId,
    eventStartTime: moment(eventStartTime).toISOString(),
    eventNotes,
    file,
  })
  if (error) {
    console.log(error)
    return
  }
  await router.push({ path: '/', query: { bookingId } })
  toast.success('Event saved')
}

const fetchEventById = async (bookingId) => {
  const { data, error } = await eventsApi.getEventById(bookingId)
  if (error) {
    router.replace('/')
    return
  }
  event.value = data
}

fetchEventById(parseInt(route.params?.bookingId))
</script>

<template>
  <div>
    <ScheduleForm :event-model="event" :is-edit-mode="true" @save-event="saveEvent" />
  </div>
</template>

<style scoped></style>
