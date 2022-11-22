import { PublicClientApplication } from '@azure/msal-browser'
const msalConfig = {
  auth: {
    clientId: '874aa2d6-f4c6-4ca2-a733-091e92495ee6',
    authority: 'https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d',
    redirectUri: 'http://localhost:3000/',
  },
  cache: {
    cacheLocation: 'sessionStorage', // This configures where your cache will be stored
    storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
  },
}

const loginRequest = {
  scopes: ['User.Read'],
}

// Add here the endpoints for MS Graph API services you would like to use.
const graphConfig = {
  graphMeEndpoint: 'https://graph.microsoft.com/v1.0/me',
  graphMailEndpoint: 'https://graph.microsoft.com/v1.0/me/messages',
}

// Add here scopes for access token to be used at MS Graph API endpoints.
const tokenRequest = {
  scopes: ['User.Read', 'Mail.Read'],
}

export const loginPopup = async () => {
  try {
    const resp = await msal.loginPopup(loginRequest)
    return resp
  } catch (error) {
    return error
  }
}

const msal = new PublicClientApplication(msalConfig)
