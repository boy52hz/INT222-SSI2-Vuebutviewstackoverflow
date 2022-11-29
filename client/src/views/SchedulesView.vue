<script setup>
import moment from 'moment'
import { useToast } from 'vue-toastification'
import AppModal from '../components/App/AppModal.vue'
import AppButton from '../components/App/AppButton.vue'
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
const isLoading = ref(false)
const isFetching = ref(true)
const events = ref([])
const selectedEvent = ref(null)

const deleteConfirmModal = ref(false)

const showDeleteConfirmModal = () => (deleteConfirmModal.value = true)
const closeDeleteConfirmModal = () => (deleteConfirmModal.value = false)

const viewEvent = (event) => (selectedEvent.value = event)
const resetSelectedEvent = () => (selectedEvent.value = null)

const deleteEvent = async ({ bookingId, bookingName }) => {
  isLoading.value = true
  const { error } = await eventsApi.deleteEventById(bookingId)
  isLoading.value = false
  closeDeleteConfirmModal()
  resetSelectedEvent()

  if (error) {
    toast.error(error.message)
    return
  }

  await fetchEvents()
  toast.success(`An event #${bookingId} ${bookingName} has been deleted.`)
}

const fetchEvents = async () => {
  isFetching.value = true
  const { data, error } = await eventsApi.getEvents()
  events.value = data

  // Show event detail if url come with query.
  if (route?.query?.bookingId) {
    const event = events.value.find((event) => event.bookingId === parseInt(route.query.bookingId))
    selectedEvent.value = event
  }

  isFetching.value = false
}

watch(
  () => authStore.isAuthenticated,
  async (isAuthenticated) => {
    if (isAuthenticated) {
      fetchEvents()
    }
  }
)

fetchEvents().finally(() => (isFetching.value = false))
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
      :class="`w-full lg:origin-left lg:transition-all lg:duration-[800ms] lg:ease-out-cubic ${
        selectedEvent && 'w-full lg:w-1/2'
      } ${!authStore.isAuthenticated && 'blur-[2px]'}`"
    >
      <ListBoxHeader>
        <div class="font-bold text-sm lg:text-base">{{ events?.length || 0 }} Event(s)</div>
      </ListBoxHeader>
      <ListBoxBody
        v-if="isFetching || !authStore.isAuthenticated"
        :class="!authStore.isAuthenticated && '!overflow-hidden'"
      >
        <Card v-for="index in 5" class="opacity-30" :key="index" :skeleton="true" />
      </ListBoxBody>
      <ListBoxBody v-else>
        <Card
          :class="`group/card hover:bg-sky-700  hover:cursor-pointer transition-all duration-300 space-y-3 ${
            selectedEvent?.bookingId === event.bookingId && 'bg-sky-700 text-white'
          }`"
          v-for="event in events"
          :key="event.bookingId"
          @click="viewEvent(event)"
        >
          <h1 class="font-bold text-sm lg:text-xl mb-2 group-hover/card:text-white">
            {{ event.bookingName }}
          </h1>
          <div>
            <div class="space-x-1 font-light lg:space-x-2 text-xs lg:text-base group-hover/card:text-white">
              <font-awesome-icon icon="fa-solid fa-calendar-alt" />
              <span>
                {{ moment(event.eventStartTime).format('LL') }} •
                {{ $getFormattedEventPeriod(event.eventStartTime, event.eventDuration) }}
              </span>
            </div>
            <div class="space-x-1 font-light lg:space-x-2 text-xs lg:text-sm group-hover/card:text-white">
              <font-awesome-icon icon="fa-solid fa-clock" /><span>{{ event.eventDuration }} Minutes</span>
            </div>
          </div>
          <ScheduleCategoryBadge :category="event.category" />
        </Card>
      </ListBoxBody>
    </ListBox>
    <ScheduleDetail
      :event-detail="selectedEvent"
      @close="resetSelectedEvent"
      @delete-event="showDeleteConfirmModal"
      :is-loading="isLoading"
    />
  </div>
  <AppModal :show="deleteConfirmModal">
    <div class="space-y-3">
      <h2 class="text-gray-500">Are you sure to cancel this event?</h2>
      <div class="text-center text-xl">#{{ selectedEvent?.bookingId }} {{ selectedEvent?.bookingName }}</div>
      <div class="flex gap-2">
        <AppButton variant="danger" @click="deleteEvent(selectedEvent)" :is-loading="isLoading">Yes</AppButton>
        <AppButton variant="light" @click="closeDeleteConfirmModal">No</AppButton>
      </div>
    </div>
  </AppModal>
</template>

<style scoped></style>
