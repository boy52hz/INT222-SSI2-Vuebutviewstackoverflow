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
const isLoading = ref(false)

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
  isLoading.value = true
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
  isLoading.value = false
  if (error) {
    toast.error(Object.values(error?.fieldErrors).join('\n') || error?.message)
    return
  }

  if (!isGuest) {
    await router.push({ path: '/', query: { bookingId: data.bookingId } })
  }
  toast.success('Your event has been booked.')
}
</script>

<template>
  <div>
    <ScheduleForm :event-model="createEventModel" @save-event="createEvent" :is-loading="isLoading" />
  </div>
</template>

<style lang="scss" scoped></style>
