<script setup>
import moment from 'moment'
import { ref, watchEffect } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import * as eventsApi from '../apis/events'
import ScheduleForm from '../components/Schedule/ScheduleForm.vue'
import { Role } from '../enums/Role'
import { useAuthenticationStore } from '../stores/authentication'

const toast = useToast()
const router = useRouter()
const authStore = useAuthenticationStore()

const createEventModelTemplate = {
  bookingName: '',
  bookingEmail: '',
  category: null,
  eventStartTime: null,
  eventNotes: '',
  file: null,
}

const createEventModel = ref({ ...createEventModelTemplate })

watchEffect(() => {
  createEventModel.value.bookingEmail = authStore.user?.email
})

const createEvent = async ({ bookingName, bookingEmail, category, eventStartTime, eventNotes, file }) => {
  const isGuest = authStore.user?.role === Role.Guest
  const { data, error } = await eventsApi.createEvent(
    {
      bookingName,
      bookingEmail,
      categoryId: category.categoryId,
      eventStartTime: moment(eventStartTime).toISOString(),
      eventNotes,
      file,
    },
    isGuest
  )
  if (error) {
    console.log(error)
    return
  }

  if (!isGuest) {
    await router.push({ path: '/', query: { bookingId: data.bookingId } })
  }
  toast.success('Your event has been booked.')
}
</script>

<template>
  <div class="container max-w-xl mx-auto">
    <ScheduleForm :event-model="createEventModel" @save-event="createEvent" />
  </div>
</template>

<style lang="scss" scoped></style>
