import axios from '../utils/axios'

export const getRegisterableRoles = async () => {
  try {
    const res = await axios('/api/roles/registerable')
    return res
  } catch (error) {
    return error
  }
}

export const getRoles = async () => {
  try {
    const res = await axios('/api/roles')
    return res
  } catch (error) {
    return error
  }
}
