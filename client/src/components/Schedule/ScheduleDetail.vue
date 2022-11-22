<script setup>
import moment from 'moment'
import { Role } from '@/enums/Role'
import { useAuthenticationStore } from '@/stores/authentication'
import AppButton from '../App/AppButton.vue'
import ScheduleCategoryBadge from './ScheduleCategoryBadge.vue'
import AppModal from '../App/AppModal.vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
  eventDetail: {
    type: Object,
    default: null,
  },
})
const emit = defineEmits(['close', 'delete-event'])

const authStore = useAuthenticationStore()
const deleteConfirmModal = ref(false)

const showDeleteConfirmModal = () => (deleteConfirmModal.value = true)
const closeDeleteConfirmModal = () => (deleteConfirmModal.value = false)

const deleteEvent = () => {
  closeDeleteConfirmModal()
  emit('delete-event', props.eventDetail)
}

const editEvent = () => {
  router.push(`/schedules/${props.eventDetail?.bookingId}`)
}
</script>

<template>
  <div
    :class="`relative bg-light flex-1 w-full rounded-lg drop-shadow ${eventDetail && 'slide-in'}`"
    v-if="eventDetail"
  >
    <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full text-center p-5 font-light">
      <h1 class="text-xl font-bold">
        {{ eventDetail?.bookingName }}
      </h1>
      <cite>({{ eventDetail.bookingEmail }})</cite>
      <ScheduleCategoryBadge class="mx-auto" :category="eventDetail.category" />
      <p class="my-3 text-gray-800">
        <font-awesome-icon icon="fa-solid fa-calendar-days" />
        {{ moment(eventDetail.eventStartTime).format('LL') }} •
        {{ $getFormattedEventPeriod(eventDetail.eventStartTime, eventDetail.eventDuration) }}
      </p>
      <cite v-show="eventDetail.eventNotes">‟ {{ eventDetail.eventNotes }} ”</cite>
      <div class="text-xs space-x-2 hover:text-secondary-blue w-fit mx-auto" v-if="eventDetail.file">
        <font-awesome-icon icon="paperclip" />
        <button class="italic" @click="downloadFile">{{ eventDetail.file.name }}</button>
      </div>
      <div class="flex justify-center gap-4 mt-6" v-if="[Role.Admin, Role.Student].includes(authStore.user?.role)">
        <AppButton class="w-fit" variant="warning" @click="editEvent">Edit Details</AppButton>
        <AppButton class="w-fit" variant="danger" @click="showDeleteConfirmModal">Cancel Event</AppButton>
      </div>
    </div>

    <font-awesome-icon
      class="text-red absolute top-5 right-5 hover:cursor-pointer"
      icon="fa-solid fa-xmark"
      @click="$emit('close')"
    />
  </div>
  <AppModal :show="deleteConfirmModal">
    <div class="space-y-3">
      <h2 class="text-gray-500">Are you sure to cancel this event?</h2>
      <div class="text-center text-xl">#{{ eventDetail?.bookingId }} {{ eventDetail?.bookingName }}</div>
      <div class="flex gap-2">
        <AppButton variant="danger" @click="deleteEvent">Yes</AppButton>
        <AppButton variant="light" @click="closeDeleteConfirmModal">No</AppButton>
      </div>
    </div>
  </AppModal>
</template>

<style scoped>
.slide-in {
  -webkit-animation: slide-in-right 0.5s cubic-bezier(0.165, 0.84, 0.44, 1) both;
  animation: slide-in-right 0.5s cubic-bezier(0.165, 0.84, 0.44, 1) both;
}

@keyframes slide-in-right {
  0% {
    -webkit-transform: translateX(200px);
    transform: translateX(200px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateX(0);
    transform: translateX(0);
    opacity: 1;
  }
}
</style>
