import { readonly, ref } from 'vue'
import { acceptHMRUpdate, defineStore } from 'pinia'
import * as authenticationApi from '../apis/authentication'
import * as msalUtil from '../utils/msal'
import { Role } from '../enums/Role'
import axios from '../utils/axios'

const initialState = readonly({
  authenticationType: '',
  user: {
    name: '',
    email: '',
    role: Role.Guest,
  },
  isAuthenticated: false,
})

export const useAuthenticationStore = defineStore('authentication', () => {
  const authenticationType = ref(initialState.authenticationType)
  const user = ref(initialState.user)
  const isAuthenticated = ref(initialState.isAuthenticated)

  const login = async ({ email, password }) => {
    const { data, error } = await authenticationApi.login({ email, password })

    if (error) {
      return { error }
    }
    setToken(data.accessToken, data.refreshToken)
    return { data }
  }

  const loginWithMs = async () => {
    try {
      const { idTokenClaims, accessToken } = await msalUtil.openLoginPopup()
      authenticate('msal', {
        name: idTokenClaims.name,
        email: idTokenClaims.preferred_username,
        role: idTokenClaims?.roles ? idTokenClaims?.roles[0] : Role.Guest,
        bearerToken: accessToken,
      })
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
    authenticationType.value = initialState.authenticationType
    isAuthenticated.value = initialState.isAuthenticated
    user.value = { ...initialState.user }
    deleteToken()
  }

  const logoutMs = () => {
    msalUtil.logout().then(() => {
      authenticationType.value = initialState.authenticationType
      isAuthenticated.value = initialState.isAuthenticated
      user.value = { ...initialState.user }
    })
  }

  const retrieveUser = async () => {
    axios.defaults.headers.common['Authorization'] = `Bearer ${getToken().accessToken}`
    console.log(axios.defaults.headers.common['Authorization'])
    const { data, error } = await authenticationApi.retrieveUser()
    if (error) throw error
    const { name, email, role } = data
    authenticate('local', {
      name,
      email,
      role: role?.label,
      bearerToken: getToken().accessToken,
    })
    return data
  }

  const authenticate = (_authenticationType, { name, email, role, bearerToken }) => {
    authenticationType.value = _authenticationType
    localStorage.setItem('authenticationType', _authenticationType)
    user.value = {
      name,
      email,
      role,
    }
    isAuthenticated.value = true
    axios.defaults.headers.common['Authorization'] = `Bearer ${bearerToken}`
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
  }

  const deleteToken = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    delete axios.defaults.headers.common['Authorization']
  }

  return {
    authenticationType,
    authenticate,
    user,
    isAuthenticated,
    login,
    loginWithMs,
    registerUser,
    retrieveUser,
    verifyAccessToken,
    refreshToken,
    logout,
    logoutMs,
    getToken,
    setToken,
    deleteToken,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthenticationStore, import.meta.hot))
}
