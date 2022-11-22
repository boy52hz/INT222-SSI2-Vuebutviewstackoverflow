import axios from '../utils/axios'

export const getEvents = async () => {
  try {
    const res = await axios('/api/events')
    return res
  } catch (error) {
    return error
  }
}

export const getEventById = async (bookingId) => {
  try {
    const res = await axios(`/api/events/${bookingId}`)
    return res
  } catch (error) {
    return error
  }
}

export const deleteEventById = async (bookingId) => {
  try {
    const res = await axios.delete(`/api/events/${bookingId}`)
    return res
  } catch (error) {
    return error
  }
}

export const createEvent = async (event, asGuest = false) => {
  const formData = new FormData()
  for (let [key, value] of Object.entries(event)) {
    formData.append(key, value)
  }
  const endpoint = asGuest ? '/api/guests' : '/api/events'
  try {
    const res = await axios.post(endpoint, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return res
  } catch (error) {
    return error
  }
}

export const editEvent = async (bookingId, event) => {
  const formData = new FormData()
  for (let [key, value] of Object.entries(event)) {
    formData.append(key, value)
  }
  try {
    const res = await axios.patch(`/api/events/${bookingId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return res
  } catch (error) {
    return error
  }
}
