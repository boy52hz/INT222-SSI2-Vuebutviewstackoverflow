import { PublicClientApplication } from '@azure/msal-browser'

const msal = new PublicClientApplication({
  auth: {
    clientId: '874aa2d6-f4c6-4ca2-a733-091e92495ee6',
    authority: 'https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d',
    redirectUri: 'http://localhost:3000/',
  },
  cache: {
    cacheLocation: 'sessionStorage', // This configures where your cache will be stored
    storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
  },
})

const loginRequest = {
  scopes: ['User.Read'],
}

export const openLoginPopup = async () => {
  try {
    const res = await msal.loginPopup(loginRequest)
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

export default msal
