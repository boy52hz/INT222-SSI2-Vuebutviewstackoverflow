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

export const setToken = (accessToken) => {
  instance.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
}

export default instance
