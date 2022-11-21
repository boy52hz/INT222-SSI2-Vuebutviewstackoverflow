import axios from 'axios'

const instance = axios.create({
  baseURL: `${import.meta.env.VITE_BASE_PATH}`,
})

instance.interceptors.response.use(
  (res) => {
    return res
  },
  (error) => {
    return Promise.reject({ error: error.response.data })
  }
)

export const setAccessToken = (accessToken) => {
  localStorage.setItem('accessToken', accessToken)
  instance.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
}

export const deleteAccessToken = () => {
  localStorage.removeItem('accessToken')
  delete instance.defaults.headers.common['Authorization']
}

export default instance
