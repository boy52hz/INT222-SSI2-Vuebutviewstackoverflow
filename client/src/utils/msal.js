import { PublicClientApplication, LogLevel } from '@azure/msal-browser'

const msal = new PublicClientApplication({
  auth: {
    clientId: import.meta.env.VITE_MSAL_CLIENT_ID,
    authority: `https://login.microsoftonline.com/${import.meta.env.VITE_MSAL_TENANT_ID}`,
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
})

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

export const openLoginPopup = async () => {
  try {
    const res = await msal.loginPopup(tokenRequest)
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

export default msal
