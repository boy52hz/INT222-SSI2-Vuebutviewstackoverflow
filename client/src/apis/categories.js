import axios from '../utils/axios'

export const getCategories = async () => {
  try {
    const res = await axios('/api/categories')
    return res
  } catch (err) {
    return err
  }
}
