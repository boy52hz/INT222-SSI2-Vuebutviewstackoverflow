import { readonly, ref } from 'vue'
import { acceptHMRUpdate, defineStore } from 'pinia'
import * as authenticationApi from '../apis/authentication'
import { openLoginPopup } from '../utils/msal'
import axios from '../utils/axios'
import { Role } from '../enums/Role'

const initialState = readonly({
  user: {
    name: '',
    email: '',
    role: Role.Guest,
  },
  isAuthenticated: false,
})

export const useAuthenticationStore = defineStore('authentication', () => {
  const user = ref(initialState.user)
  const isAuthenticated = ref(initialState.isAuthenticated)

  const login = async ({ email, password }) => {
    const { data, error } = await authenticationApi.login({ email, password })
    if (error) {
      return { error }
    }
    setToken(data.accessToken, data.refreshToken)
    isAuthenticated.value = true
    return { data }
  }

  const loginWithMs = async () => {
    try {
      const { idTokenClaims, accessToken } = await openLoginPopup()
      console.log(idTokenClaims)
      user.value = {
        name: idTokenClaims.name,
        email: idTokenClaims.preferred_username,
        role: idTokenClaims?.roles[0] || 'Guest',
      }
      isAuthenticated.value = true
      setToken(accessToken)
      return true
    } catch (error) {
      return { error }
    }
  }

  const registerUser = async ({ name, email, password, roleName }) => {
    const { data, error } = await authenticationApi.register({ name, email, password, roleName })
    if (error) {
      console.log(error)
      return { error }
    }
    return { data }
  }

  const logout = () => {
    isAuthenticated.value = initialState.isAuthenticated
    user.value = { ...initialState.user }
    deleteToken()
  }

  const retrieveUser = async () => {
    const { data, error } = await authenticationApi.retrieveUser()
    if (error) throw error
    const { name, email, role } = data
    user.value = {
      name,
      email,
      role: role.label,
    }
    isAuthenticated.value = true
    return data
  }

  const getToken = () => {
    return { accessToken: localStorage.getItem('accessToken'), refreshToken: localStorage.getItem('refreshToken') }
  }

  const setToken = (accessToken, refreshToken = null) => {
    localStorage.setItem('accessToken', accessToken)
    if (refreshToken) {
      localStorage.setItem('refreshToken', refreshToken)
    }
    axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
  }

  const deleteToken = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    delete axios.defaults.headers.common['Authorization']
  }

  return {
    user,
    isAuthenticated,
    login,
    loginWithMs,
    registerUser,
    retrieveUser,
    logout,
    getToken,
    setToken,
    deleteToken,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthenticationStore, import.meta.hot))
}
