import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { computed, ref } from 'vue'
import { deleteToken, setToken } from '../utils/token'

export const useUser = defineStore('user', () => {
  const user = ref({})
  const token = ref(localStorage.getItem('token'))
  const isAuthenticated = ref(false)

  const loadUser = async () => {
    const existToken = getToken()
    if (!isAuthenticated.value && existToken) {
      setToken(existToken)
    }

    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/login`)
      user.value = res.data
      isAuthenticated.value = true
    } catch (err) {
      throw err
    }
  }

  const loginUser = async (loginData) => {
    try {
      const res = await axios.post(`${import.meta.env.VITE_BASE_PATH}/api/login`, loginData)
      const data = res.data
      const bearerToken = `Bearer ${data.token}`
      token.value = bearerToken
      isAuthenticated.value = true
      setToken(bearerToken)
      await loadUser()
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
    isAuthenticated.value = false
    deleteToken()
  }

  const getToken = () => localStorage.getItem('token')

  return {
    user,
    loginUser,
    registerUser,
    loadUser,
    logout,
    getToken,
    isAuthenticated,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUser, import.meta.hot))
}
