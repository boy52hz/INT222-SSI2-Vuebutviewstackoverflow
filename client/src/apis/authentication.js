import axios from '../utils/axios'

export const login = async ({ email, password }) => {
  try {
    const res = await axios.post('/api/auth', { email, password })
    return res
  } catch (error) {
    console.log(error)
    return error
  }
}

export const register = async ({ name, email, password, roleName }) => {
  try {
    const res = await axios.post('/api/auth/register', {
      name,
      email,
      password,
      role: roleName,
    })
    return res
  } catch (error) {
    console.log(error)
    return error
  }
}

export const refreshToken = async (refreshToken) => {
  try {
    const res = await axios.post('/api/auth/refreshToken', {
      refreshToken,
    })
    return res
  } catch (error) {
    return error
  }
}

export const retrieveUser = async () => {
  try {
    const res = await axios('/api/auth')
    return res
  } catch (error) {
    return error
  }
}

export const verifyAccessToken = async (accessToken) => {
  if (!accessToken || accessToken === 'null') return false
  try {
    await axios('/api/auth/verify', {
      params: { accessToken },
    })
    return true
  } catch (error) {
    return false
  }
}
