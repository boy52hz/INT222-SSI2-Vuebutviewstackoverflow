import { PublicClientApplication, LogLevel } from '@azure/msal-browser'

export const msalConfig = {
  auth: {
    clientId: import.meta.env.VITE_MSAL_CLIENT_ID,
    authority: `https://login.microsoftonline.com/${import.meta.env.VITE_MSAL_TENANT_ID}`,
    redirectUri: '/',
    postLogoutRedirectUri: '/',
  },
  cache: {
    cacheLocation: 'localStorage', // This configures where your cache will be stored
    storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
  },
  system: {
    loggerOptions: {
      logLevel: LogLevel.Trace,
    },
  },
}

export const apiConfig = {
  apiEndpoint: import.meta.env.BASE_URL + 'api',
  scopes: [`api://${import.meta.env.VITE_MSAL_CLIENT_ID}/access_as_user`],
}

export const loginRequest = {
  scopes: ['openid', 'profile'],
}

export const tokenRequest = {
  scopes: [...apiConfig.scopes],
}

const msal = new PublicClientApplication(msalConfig)

export const openLoginPopup = async () => {
  try {
    const res = await msal.loginPopup(tokenRequest)
    return res
  } catch (err) {
    return err
  }
}

export const logout = () => {
  return msal.logoutPopup({
    account: msal.getActiveAccount(),
    redirectUri: msalConfig.auth.redirectUri,
    postLogoutRedirectUri: msalConfig.auth.postLogoutRedirectUri,
  })
}

export default msal
