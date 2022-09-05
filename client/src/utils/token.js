import axios from 'axios'

export const setToken = (token) => {
  if (!token) return
  axios.defaults.headers.common['Authorization'] = token
  localStorage.setItem('token', token)
}

export const deleteToken = () => {
  delete axios.defaults.headers.common['Authorization']
  localStorage.removeItem('token')
}
