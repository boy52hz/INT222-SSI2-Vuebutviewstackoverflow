import axios, { setToken } from '../utils/axios'

export const login = async ({ email, password }) => {
  try {
    const res = await axios.post('/api/auth', { email, password })
    setToken(res.data.accessToken)
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
