import axios from '../utils/axios'

export const getUsers = async () => {
  try {
    const res = await axios('/api/users')
    return res
  } catch (error) {
    return error
  }
}

export const getUserById = async (userId) => {
  try {
    const res = await axios(`/api/users/${userId}`)
    return res
  } catch (error) {
    return error
  }
}

export const editUser = async (userId, { name, email, roleName }) => {
  try {
    const res = await axios.patch(`/api/users/${userId}`, {
      name,
      email,
      role: roleName,
    })
    return res
  } catch (error) {
    return error
  }
}

export const deleteUser = async (userId) => {
  try {
    const res = await axios.delete(`/api/users/${userId}`)
    return res
  } catch (error) {
    return error
  }
}
