<script setup>
import { ref } from 'vue'
import AppButton from '../App/AppButton.vue'
import AppInput from '../App/AppInput.vue'
const loginForm = ref({
  email: '',
  password: '',
})
defineEmits(['login', 'login-ms'])
</script>

<template>
  <form class="space-y-3" @submit.prevent="$emit('login', loginForm)">
    <h2 class="text-2xl text-center">Login</h2>
    <div>
      <label for="email" class="required">Email</label>
      <AppInput v-model.trim="loginForm.email" id="email" name="email" type="email" :required="true" />
    </div>
    <div>
      <label for="password" class="required">Password</label>
      <AppInput v-model="loginForm.password" id="password" name="password" type="password" :required="true" />
    </div>
    <div class="space-y-3">
      <AppButton variant="primary" type="submit">Login</AppButton>
      <AppButton
        class="flex justify-center items-center gap-3"
        variant="light"
        type="button"
        @click="$emit('login-ms')"
      >
        <font-awesome-icon icon="fa-brands fa-microsoft" /><span>Sign-in with Microsoft</span>
      </AppButton>
    </div>
  </form>
</template>

<style scoped></style>
