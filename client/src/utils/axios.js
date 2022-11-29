import axios from 'axios'

const instance = axios.create({
  baseURL: import.meta.env.BASE_URL,
})

instance.interceptors.response.use(
  (res) => {
    return res
  },
  (error) => {
    return Promise.reject({ error: error.response.data })
  }
)

export default instance
