import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { computed, ref } from 'vue'
import { deleteToken, setToken } from '../utils/token'

export const useUser = defineStore('user', () => {
  const user = ref({})
  const token = ref(localStorage.getItem('token'))

  const loadUser = async () => {
    if (token.value) {
      setToken(token.value)
    }
    // try {
    //   const res = await axios.get('/')
    // }
  }

  const loginUser = async (loginData) => {
    try {
      const res = await axios.post(`${import.meta.env.VITE_BASE_PATH}/api/users/match`, loginData)
      const data = res.data
      const bearerToken = `Bearer ${data.token}`
      token.value = bearerToken
      setToken(bearerToken)
    } catch (err) {
      throw err.response.data
    }
  }

  const registerUser = async (registerData) => {
    try {
      await axios.post(`${import.meta.env.VITE_BASE_PATH}/api/users`, registerData)
    } catch (err) {
      throw err.response.data
    }
  }

  const logout = () => {
    user.value = {}
    token.value = null
    deleteToken()
  }

  const getToken = computed(() => {
    return token.value
  })

  return {
    user,
    loginUser,
    registerUser,
    loadUser,
    logout,
    getToken,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUser, import.meta.hot))
}
