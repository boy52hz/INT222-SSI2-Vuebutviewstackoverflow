<script setup>
import { ref } from 'vue'

const inputValue = ref('')
defineProps({
  required: {
    type: Boolean,
    default: false,
  },
  labelText: {
    type: String,
  },
  errorMessage: {
    type: String,
  },
})
</script>

<template>
  <div class="input-group">
    <label v-show="labelText" :for="$attrs.id" :class="required && 'required'">{{ labelText }}</label>
    <input
      v-bind="$attrs"
      :class="errorMessage && 'error'"
      @input="(evt) => $emit('update:modelValue', evt.target.value)"
    />
    <div v-show="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<style scoped>
.input-group input {
  width: 100%;
}
.input-group input.error {
  border-color: red;
}
.input-group label {
  font-size: 0.8em;
}
.error-message {
  color: red;
  font-size: 0.7em;
}
</style>
