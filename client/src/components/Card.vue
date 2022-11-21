<script setup>
defineProps({
  skeleton: {
    type: Boolean,
    default: false,
  },
})
</script>

<template>
  <div :class="['bg-white rounded p-5 drop-shadow', skeleton && 'h-fit']">
    <div role="status" class="max-w-sm animate-pulse" v-if="skeleton">
      <div class="h-2.5 bg-gray-200 rounded-full dark:bg-gray-300 w-48 mb-4"></div>
      <div class="h-2 bg-gray-200 rounded-full dark:bg-gray-300 max-w-[360px] mb-2.5"></div>
      <div class="h-2 bg-gray-200 rounded-full dark:bg-gray-300 mb-2.5"></div>
      <div class="h-2 bg-gray-200 rounded-full dark:bg-gray-300 max-w-[330px] mb-2.5"></div>
      <span class="sr-only">Loading...</span>
    </div>
    <slot v-else />
  </div>
</template>

<style></style>
