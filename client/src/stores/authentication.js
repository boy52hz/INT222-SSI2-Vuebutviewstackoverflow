import { ref } from 'vue'
import { acceptHMRUpdate, defineStore } from 'pinia'
import * as authenticationApi from '../apis/authentication'
import msal, { openLoginPopup } from '../utils/msal'
import { setToken } from '../utils/axios'

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
      setToken(accessToken)
      isAuthenticated.value = true
    } catch (err) {
      console.log(err)
    }
  }

  const retrieveUser = async () => {
    const res = await authenticationApi.retrieveUser()
    if (res.status === 200) {
      const { name, email, role } = res.data
      user.value = {
        name,
        email,
        role: role.name.toUpperCase(),
      }
    }
  }

  return { user, isAuthenticated, login, loginWithMs, retrieveUser }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthenticationStore, import.meta.hot))
}
