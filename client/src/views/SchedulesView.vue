<script setup>
import moment from 'moment'
import ListBox from '../components/ListBox/ListBox.vue'
import ListBoxBody from '../components/ListBox/ListBoxBody.vue'
import ListBoxHeader from '../components/ListBox/ListBoxHeader.vue'
import Card from '../components/Card.vue'
import * as eventsApi from '../apis/events'
import { onMounted, ref, watch } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'

const authStore = useAuthenticationStore()
const events = ref([])

const fetchEvents = async () => {
  const { data, error } = await eventsApi.getEvents()
  events.value = data
}

watch(
  () => authStore.isAuthenticated,
  async (isAuthenticated) => {
    if (isAuthenticated) {
      fetchEvents()
    }
  }
)

if (authStore.isAuthenticated) {
  fetchEvents()
}
</script>

<template>
  <div class="w-full h-full">
    <ListBox>
      <ListBoxHeader>
        <div class="font-bold text-sm lg:text-base">0 Event(s)</div>
      </ListBoxHeader>
      <ListBoxBody>
        <Card
          class="hover:bg-sky-700 hover:text-white hover:cursor-pointer transition-all duration-300"
          v-for="event in events"
          :key="event.bookingId"
          @click="showEventDetail(event.bookingId)"
        >
          <h1 class="font-bold text-sm lg:text-xl">{{ event.bookingName }}</h1>
          <div class="space-x-1 lg:space-x-2 text-xs lg:text-base">
            <font-awesome-icon icon="fa-solid fa-calendar-alt" />
            <span>
              {{ moment(event.eventStartTime).format('LL') }} •
              {{ $getFormattedEventPeriod(event.eventStartTime, event.eventDuration) }}
            </span>
          </div>
          <div class="space-x-1 lg:space-x-2 text-xs lg:text-base">
            <font-awesome-icon icon="fa-solid fa-clock" /><span>{{ event.eventDuration }} Minutes</span>
          </div>
          <div class="space-x-1 lg:space-x-2 text-xs lg:text-base">
            {{ event.category.categoryName }}
          </div>
        </Card>
      </ListBoxBody>
    </ListBox>
  </div>
</template>

<style scoped></style>
