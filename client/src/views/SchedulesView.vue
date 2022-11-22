<script setup>
import moment from 'moment'
import { useToast } from 'vue-toastification'
import ListBox from '../components/ListBox/ListBox.vue'
import ListBoxBody from '../components/ListBox/ListBoxBody.vue'
import ListBoxHeader from '../components/ListBox/ListBoxHeader.vue'
import ScheduleDetail from '../components/Schedule/ScheduleDetail.vue'
import Card from '../components/Card.vue'
import * as eventsApi from '../apis/events'
import { ref, watch } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'
import ScheduleCategoryBadge from '../components/Schedule/ScheduleCategoryBadge.vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const toast = useToast()
const authStore = useAuthenticationStore()
const isLoading = ref(true)
const events = ref([])
const selectedEvent = ref(null)

const viewEvent = (event) => (selectedEvent.value = event)
const resetSelectedEvent = () => (selectedEvent.value = null)

const deleteEvent = async ({ bookingId, bookingName }) => {
  isLoading.value = true
  resetSelectedEvent()
  const { data, error } = await eventsApi.deleteEventById(bookingId)
  if (error) {
    toast.error(error.message)
    return
  }
  isLoading.value = false

  await fetchEvents()
  toast.success(`An event #${bookingId} ${bookingName} has been deleted.`)
}

const fetchEvents = async () => {
  isLoading.value = true
  const { data, error } = await eventsApi.getEvents()
  events.value = data

  // Show event detail if url come with query.
  if (route?.query?.bookingId) {
    const event = events.value.find((event) => event.bookingId === parseInt(route.query.bookingId))
    selectedEvent.value = event
  }

  isLoading.value = false
}

watch(
  () => authStore.isAuthenticated,
  async (isAuthenticated) => {
    if (isAuthenticated) {
      fetchEvents()
    }
  }
)

fetchEvents().finally(() => (isLoading.value = false))
</script>

<template>
  <div class="relative flex w-full h-full overflow-x-hidden gap-3">
    <div
      v-if="!authStore.isAuthenticated"
      class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 z-10 text-center lg:text-xl"
    >
      Authentication is required for view schedule list.
    </div>
    <ListBox
      :class="`w-full box-animate ${selectedEvent && 'resize-left'} ${!authStore.isAuthenticated && 'blur-[2px]'}`"
    >
      <ListBoxHeader>
        <div class="font-bold text-sm lg:text-base">{{ events?.length || 0 }} Event(s)</div>
      </ListBoxHeader>
      <ListBoxBody
        v-if="isLoading || !authStore.isAuthenticated"
        :class="!authStore.isAuthenticated && '!overflow-hidden'"
      >
        <Card v-for="index in 10" class="opacity-30" :key="index" :skeleton="true" />
      </ListBoxBody>
      <ListBoxBody v-else>
        <Card
          :class="`hover:bg-sky-700 hover:text-white hover:cursor-pointer transition-all duration-300 ${
            selectedEvent?.bookingId === event.bookingId && 'bg-sky-700 text-white'
          }`"
          v-for="event in events"
          :key="event.bookingId"
          @click="viewEvent(event)"
        >
          <h1 class="font-bold text-sm lg:text-xl mb-2">{{ event.bookingName }}</h1>
          <div class="space-x-1 font-light lg:space-x-2 text-xs lg:text-base">
            <font-awesome-icon icon="fa-solid fa-calendar-alt" />
            <span>
              {{ moment(event.eventStartTime).format('LL') }} •
              {{ $getFormattedEventPeriod(event.eventStartTime, event.eventDuration) }}
            </span>
          </div>
          <div class="space-x-1 font-light lg:space-x-2 text-xs lg:text-base">
            <font-awesome-icon icon="fa-solid fa-clock" /><span>{{ event.eventDuration }} Minutes</span>
          </div>
          <ScheduleCategoryBadge :category="event.category" />
        </Card>
      </ListBoxBody>
    </ListBox>
    <ScheduleDetail :event-detail="selectedEvent" @close="resetSelectedEvent" @delete-event="deleteEvent" />
  </div>
</template>

<style scoped>
.box-animate {
  transition-delay: 1000ms;
  transform-origin: left;
  transition: all 0.5s cubic-bezier(0.215, 0.61, 0.355, 1);
}

.resize-left {
  width: 50%;
}
</style>
