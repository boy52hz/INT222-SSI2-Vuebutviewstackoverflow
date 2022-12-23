<script setup>
const props = defineProps({
  modelValue: {},
  modelModifiers: { default: () => ({}) },
  errorMessage: {
    type: String,
    default: '',
  },
  required: {
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
  <div class="space-y-1">
    <input
      :class="`app-input ${errorMessage !== '' && '!border-red-500'}`"
      v-bind="$attrs"
      :value="modelValue"
      @input="emitValue"
      :required="required"
    />
    <div
      :class="`text-red-500 text-xs transition-all duration-200 text-justify ${
        errorMessage !== '' ? 'opacity-100' : 'opacity-0'
      }`"
    >
      {{ errorMessage }}
    </div>
  </div>
</template>

<style lang="scss" scoped>
.app-input {
  @apply py-1 px-2 border border-[#0a0a0a33] transition-colors duration-300 w-full;
  &:focus {
    @apply outline-none border-[#247ba0];
  }
}
</style>
