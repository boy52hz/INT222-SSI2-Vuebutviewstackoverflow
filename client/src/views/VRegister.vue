<script setup>
import { reactive, ref } from 'vue'
import { useUsers } from '../stores/users'
import AppInput from '../components/App/AppInput.vue'

const userStore = useUsers()

const roles = reactive(['student', 'lecturer', 'admin'])

const message = ref('')

const formRegister = ref({
  email: '',
  password: '',
  confirmPassword: '',
  firstName: '',
  lastName: '',
  role: roles[0],
})

const errorMessage = ref({
  email: '',
  password: '',
  confirmPassword: '',
  firstName: '',
  lastName: '',
  role: '',
})

const regisUser = async () => {
  try {
    const data = await userStore.registerUser({
      email:formRegister.value.email,
      name:formRegister.value.firstName+" "+formRegister.value.lastName,
      role:formRegister.value.role,
      password:formRegister.value.password
    })
  } catch (err) {
    alert(err.message)
  }
}

const passConfirm = () => {
  if ( formRegister.value.password === formRegister.value.confirmPassword && formRegister.value.password.length >= 8 && formRegister.value.confirmPassword.length >= 8){
    message.value = "match"
  } 
  else if (formRegister.value.password != formRegister.value.confirmPassword && formRegister.value.password.length >= 8 && formRegister.value.confirmPassword.length >= 8){
    message.value = "dmatch"
  }else {
    message.value = null
  }
}
</script>

<template>
  <div class="container">
    <div class="wrapper">
      <div class="title">Welcome to OASIP-SSI2</div>
      <div class="desc">Please, fill form to create account.</div>
      <form @submit.prevent="regisUser">
        <AppInput
          type="text"
          v-model="formRegister.firstName"
          label-text="First Name"
          :error-message="errorMessage.firstName"
          :required="true"
        />
        <AppInput
          type="text"
          v-model="formRegister.lastName"
          label-text="Last Name"
          :error-message="errorMessage.lastName"
          :required="true"
        />
        <AppInput
          type="email"
          pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
          v-model="formRegister.email"
          label-text="Email"
          :error-message="errorMessage.email"
          :required="true"
        />
        <AppInput
          type="password"
          v-model="formRegister.password"
          label-text="Password"
          :error-message="errorMessage.password"
          :required="true"
          :onkeyup="passConfirm"
          :minlength="8"
        />
        <AppInput
          type="password"
          v-model="formRegister.confirmPassword"
          label-text="Confirm Password"
          :error-message="errorMessage.confirmPassword"
          :required="true"
          :onkeyup="passConfirm"
          :minlength="8"
        />
        <span class="message" v-if="message=='match'">Passwords Match!</span>
        <span class="dmessage" v-if="message=='dmatch'">Passwords Doesn't Match!</span>
        <div class="input-group">
          <label class="required">Role</label>
          <select v-model="formRegister.role" style="width: 100%">
            <option v-for="(role, index) in roles" :key="index" :value="role">{{ $capitalize(role) }}</option>
          </select>
        </div>
        <app-button class="btn-submit" type="submit">Create New Account</app-button>
      </form>
      <router-link class="register-href" to="/login">I already have an account</router-link>
    </div>
  </div>
</template>

<style scoped>
.message {
  color: green;
  font-size: 0.8em;
}

.dmessage {
  color: red;
  font-size: 0.8em;
}
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

.title {
  font-weight: 300;
}

.desc {
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
