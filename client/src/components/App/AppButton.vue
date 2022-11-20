<script setup>
import { ref } from 'vue'
const props = defineProps({
  variant: {
    type: String,
    default: 'primary',
  },
})
const theme = ref({
  primary: 'bg-primary-blue text-white',
  light: 'bg-slate-200 text-slate-800',
})
</script>

<template>
  <button v-bind="$attrs" :class="['block py-2 px-4 rounded-md w-full', theme[variant]]"><slot /></button>
</template>

<style scoped></style>
