<script setup>
import { Transition, ref, computed } from 'vue'
const emits = defineEmits(['confirm', 'cancel', 'ok'])
const props = defineProps({
  show: { type: Boolean, default: false },
  modalType: {
    type: String,
    default: 'blank',
  },
})
const modal = computed(() => ({ show: props.show, type: props.modalType }))
const closeModal = () => {
  emits('cancel')
}
</script>

<template>
  <Transition name="fade">
    <div @click="closeModal" v-if="modal.show" class="modal">
      <Transition name="bounce" appear>
        <div @click="(evt) => evt.stopPropagation()" v-if="modal.show" class="modal-box">
          <slot />
        </div>
      </Transition>
    </div>
  </Transition>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  background-color: rgb(0, 0, 0, 0.5);
  height: 100%;
  width: 100%;
  z-index: 999;

  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-box {
  background-color: whitesmoke;
  padding: 20px 32px;
  border-radius: 10px;
  box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
}
.modal-input-group {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
}
.modal-input-group > button {
  width: 60px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.1s ease-out;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.bounce-enter-active {
  animation: bounce-in 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}

@keyframes bounce-in {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
