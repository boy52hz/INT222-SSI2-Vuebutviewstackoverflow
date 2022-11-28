<script setup>
import { ref } from 'vue'
const emit = defineEmits(['input'])

const file = ref(null)

const handleUpload = (evt) => {
  const inputFile = evt.target.files[0]
  if (inputFile) {
    file.value = evt.target.files[0]
    emit('input', file)
  }
}
</script>

<template>
  <div class="max-w-xl">
    <label
      class="relative flex justify-center w-full h-20 px-4 transition bg-white border-2 border-gray-300 border-dashed rounded-md appearance-none cursor-pointer hover:border-gray-400 focus:outline-none"
    >
      <span class="flex items-center space-x-2">
        <font-awesome-icon v-if="!file" class="w-4 h-4 text-gray-600" icon="fa-solid fa-upload" />
        <font-awesome-icon v-else class="w-4 h-4 text-gray-600" icon="fa-solid fa-paperclip" />
        <span v-if="!file" class="font-medium text-gray-600">Upload file</span>
        <div v-else>
          <span class="text-gray-600"
            >{{ file?.name }}
            <cite class="text-gray-600 text-xs">({{ (file?.size / (1024 * 1024)).toFixed(2) }} MB)</cite></span
          >
        </div>
      </span>
      <input v-bind="$attrs" type="file" name="file-upload" class="hidden" @change="handleUpload" />
    </label>
  </div>
</template>

<style scoped></style>
