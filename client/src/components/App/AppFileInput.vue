<script setup>
import { ref, watch } from 'vue'
const emit = defineEmits(['input'])
const props = defineProps({
  fileModel: {
    type: Object,
    default: {},
  },
})

const file = ref(null)
const fileInputRef = ref(null)

watch(
  () => props.fileModel,
  (fileModel) => {
    file.value = fileModel
  }
)

const handleUpload = (evt) => {
  const inputFile = evt.target.files[0]
  if (inputFile) {
    file.value = evt.target.files[0]
    emit('input', file)
  }
}

const removeFile = (evt) => {
  file.value = null
  fileInputRef.value.value = null
  emit('input', file)
}
</script>

<template>
  <div
    class="max-w-xl flex justify-center items-center transition bg-white border-2 border-gray-300 hover:border-gray-400 border-dashed rounded-md focus:outline-none"
  >
    <label class="relative flex justify-center w-full h-20 px-4 appearance-none cursor-pointer hover:border-gray-400">
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
      <input ref="fileInputRef" v-bind="$attrs" type="file" name="file-upload" class="hidden" @change="handleUpload" />
    </label>
    <div v-if="file" class="text-lg text-red-500 hover:cursor-pointer p-5" @click="removeFile">
      <font-awesome-icon :width="30" icon="fa-solid fa-trash-alt" />
    </div>
  </div>
</template>

<style scoped></style>
