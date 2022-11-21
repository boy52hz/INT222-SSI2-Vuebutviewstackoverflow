import { ref } from 'vue'
import { acceptHMRUpdate, defineStore } from 'pinia'
import * as authenticationApi from '../apis/authentication'
import msal, { openLoginPopup } from '../utils/msal'
import { setAccessToken } from '../utils/axios'

export const useAuthenticationStore = defineStore('authentication', () => {
  const user = ref({
    name: '',
    email: '',
    role: '',
  })
  const isAuthenticated = ref(false)

  const login = async ({ email, password }) => {
    const { data, error } = await authenticationApi.login({ email, password })
    if (data) {
      isAuthenticated.value = true
      return { data }
    }
    console.log(error)
    return { error }
  }

  const loginWithMs = async () => {
    try {
      const { idTokenClaims, accessToken } = await openLoginPopup()
      user.value = {
        name: idTokenClaims.name,
        email: idTokenClaims.preferred_username,
        role: idTokenClaims?.roles[0] || 'Guest',
      }
      setAccessToken(accessToken)
    } catch (err) {
      console.log(err)
    }
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

  return { user, isAuthenticated, login, loginWithMs, retrieveUser }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthenticationStore, import.meta.hot))
}
