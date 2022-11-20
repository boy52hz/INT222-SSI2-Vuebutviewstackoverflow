import axios from '../utils/axios'

export const getEvents = async () => {
  try {
    const res = await axios('/api/events')
    return res
  } catch (error) {
    return error
  }
}
