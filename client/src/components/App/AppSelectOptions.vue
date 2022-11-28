<script setup>
const props = defineProps({
  modelValue: {},
  modelModifiers: { default: () => ({}) },
  options: {
    type: Array,
    default: [],
  },
})

const emit = defineEmits(['update:modelValue'])

const emitValue = (evt) => {
  let value = evt.target.value
  if (props.modelModifiers.trim) {
    value = value.trim()
  }
  emit('update:modelValue', value)
}
</script>

<template>
  <select class="app-select-options" :value="modelValue" @input="emitValue">
    <option v-for="option in options" :key="option.value" :value="option.value">
      {{ option?.label }}
    </option>
  </select>
</template>

<style lang="scss" scoped>
.app-select-options {
  @apply py-1 px-2 border border-[#0a0a0a33] transition-colors duration-300 w-full;
  &:focus {
    @apply outline-none border-[#247ba0];
  }
}
</style>
