<script setup>
import { ref } from 'vue'
import LoadingSpinner from '../LoadingSpinner.vue'
const props = defineProps({
  variant: {
    type: String,
    default: 'primary',
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
})
const theme = ref({
  primary: 'bg-primary-blue text-white hover:bg-primary-blue-hover',
  light: 'bg-slate-300 hover:bg-slate-200 text-slate-800',
  danger: 'bg-red-700 hover:bg-red-800 text-white',
  warning: 'bg-amber-500 hover:bg-amber-600 text-white',
})
</script>

<template>
  <button
    v-bind="$attrs"
    :class="[
      'block py-2 px-4 rounded-md w-full transition-all duration-300 hover:cursor-pointer disabled:opacity-70 disabled:pointer-events-none',
      theme[variant],
    ]"
    :disabled="isLoading"
  >
    <slot v-if="!isLoading" />
    <div v-else class="flex justify-center items-center">
      <LoadingSpinner />
      <span>Processing...</span>
    </div>
  </button>
</template>

<style scoped></style>
