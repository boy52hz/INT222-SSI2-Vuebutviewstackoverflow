<script setup>
import moment from 'moment'
defineProps({
  user: {
    type: Object,
    default: {},
  },
})
</script>

<template>
  <div class="space-y-3">
    <div class="text-lg font-bold">[{{ user.role.label.toUpperCase() }}] {{ user.name }}</div>
    <cite>{{ user.email }}</cite>
    <div class="text-sm">
      <div><span class="font-bold">Created:</span> {{ moment(user.createdOn).format('YYYY-MM-DD hh:mm A') }}</div>
      <div><span class="font-bold">Last updated:</span> {{ moment(user.updatedOn).format('YYYY-MM-DD hh:mm A') }}</div>
    </div>
  </div>
</template>

<style scoped></style>
