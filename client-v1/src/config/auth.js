export const msalConfig = {
  auth: {
    clientId: '874aa2d6-f4c6-4ca2-a733-091e92495ee6',
    authority: 'https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d',
    redirectUri: 'http://localhost:3000',
  },
  cache: {
    cacheLocation: 'localStorage', // This configures where your cache will be stored
    storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
  },
}

export const loginRequest = {
  scopes: ['openid', 'profile'], // e.g. ["scp1", "scp2"]
}

export const tokenRequest = {
  scopes: ['Mail.Read'],
  forceRefresh: false, // Set this to "true" to skip a cached token and go to the server to get a new token
}

export const graphConfig = {
  graphMeEndpoint: 'https://graph.microsoft.com/v1.0/me',
  graphMailEndpoint: 'https://graph.microsoft.com/v1.0/me/messages',
}
