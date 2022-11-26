import axios, { setAccessToken } from '../utils/axios'

export const login = async ({ email, password }) => {
  try {
    const res = await axios.post('/api/auth', { email, password })
    setAccessToken(res.data.accessToken)
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

export const retrieveUser = async () => {
  try {
    const res = await axios('/api/auth')
    return res
  } catch (error) {
    return error
  }
}
