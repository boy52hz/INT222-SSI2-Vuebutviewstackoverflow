import { useAuthenticationStore } from './stores/authentication'
import msal, { tokenRequest } from './utils/msal'

export const setupAuthentication = () => {
  const authStore = useAuthenticationStore()
  const authenticationType = localStorage.getItem('authenticationType')

  switch (authenticationType) {
    case 'msal':
      const msAccounts = msal.getAllAccounts()
      if (msAccounts.length > 0) {
        msal.setActiveAccount(msAccounts[0])
      }
      const request = { account: msal.getActiveAccount(), ...tokenRequest }

      msal
        .acquireTokenSilent(request)
        .then((authResult) => {
          const idTokenClaims = msal.getAccountByHomeId(authResult.account.homeAccountId).idTokenClaims
          authStore.authenticate('msal', {
            name: idTokenClaims.name,
            email: idTokenClaims.preferred_username,
            role: idTokenClaims.roles[0],
            bearerToken: authResult.accessToken,
          })
        })
        .catch((error) => {
          console.warn('silent token acquisition fails. acquiring token using popup')
        })
      break
    case 'local':
      const { accessToken } = authStore.getToken()
      authStore.setToken(accessToken)
      break
    default:
      return
  }
}
