import { readonly, ref } from 'vue'
import { acceptHMRUpdate, defineStore } from 'pinia'
import * as authenticationApi from '../apis/authentication'
import { openLoginPopup } from '../utils/msal'
import { Role } from '../enums/Role'
import axios from '../utils/axios'

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

  const verifyAccessToken = async () => {
    const { accessToken } = getToken()
    const validated = await authenticationApi.verifyAccessToken(accessToken)
    return validated
  }

  const refreshToken = async () => {
    const { refreshToken } = getToken()

    delete axios.defaults.headers.common['Authorization']

    const res = await authenticationApi.refreshToken(refreshToken)
    if (res.data) {
      setToken(res.data.accessToken, res.data.refreshToken)
    }
    return res
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
    delete axios.defaults.headers.common['Authorization']
    localStorage.clear()
  }

  return {
    user,
    isAuthenticated,
    login,
    loginWithMs,
    registerUser,
    retrieveUser,
    verifyAccessToken,
    refreshToken,
    logout,
    getToken,
    setToken,
    deleteToken,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthenticationStore, import.meta.hot))
}
