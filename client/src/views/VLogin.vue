<script setup>
import { ref } from 'vue'
import { useUsers } from '../stores/users'
import AppInput from '../components/App/AppInput.vue'

const userStore = useUsers()

const formLogin = ref({
  email: '',
  password: '',
})

const errorMessage = {
  email: '',
  password: '',
}
</script>

<template>
  <div class="container">
    <div class="wrapper">
      <div class="welcome-title">Welcome to OASIP-SSI2</div>
      <div class="welcome-desc">Please, fill form to login.</div>
      <form @submit.prevent="userStore.loginUser(formLogin)">
        <AppInput
          type="email"
          pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
          v-model="formLogin.email"
          label-text="Email"
          :error-message="errorMessage.email"
          :required="true"
        />
        <AppInput
          type="password"
          v-model="formLogin.password"
          label-text="Password"
          :error-message="errorMessage.password"
          :required="true"
        />
        <app-button class="btn-submit" type="submit">Login</app-button>
      </form>
      <router-link class="register-href" to="/register">I don't have an account</router-link>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 735px;
  width: 100%;
  margin: 0 auto;
  height: 100%;
}

.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  min-height: 100%;
  margin: 0 auto;
}

.welcome-title {
  font-weight: 300;
}

.welcome-desc {
  font-weight: 300;
  font-size: 0.8em;
  margin: 5px auto;
  opacity: 0.7;
}

.register-href {
  font-size: 0.7em;
  margin-top: 10px;
}

input {
  width: 100%;
}

form label {
  font-size: 0.8em;
}

form .btn-submit {
  width: 100%;
  margin-top: 2em;
  margin-left: auto;
  margin-right: auto;
}
</style>
