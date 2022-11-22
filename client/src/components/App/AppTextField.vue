<script setup>
const props = defineProps({
  modelValue: {},
  modelModifiers: { default: () => ({}) },
  required: {
    type: Boolean,
    default: false,
  },
  resize: {
    type: Boolean,
    default: false,
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
  <textarea
    class="app-input"
    :class="[!resize && 'resize-none']"
    v-bind="$attrs"
    :value="modelValue"
    @input="emitValue"
    :required="required"
  />
</template>

<style lang="scss" scoped>
.app-input {
  @apply py-1 px-2 border border-[#0a0a0a33] transition-colors duration-300 w-full;
  &:focus {
    @apply outline-none border-[#247ba0];
  }
}
</style>
