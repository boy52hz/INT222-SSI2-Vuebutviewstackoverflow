import axios from '../utils/axios'

export const getCategories = async () => {
  try {
    const res = await axios('/api/categories')
    return res
  } catch (error) {
    return error
  }
}

export const getCategoryById = async (categoryId) => {
  try {
    const res = await axios(`/api/categories/${categoryId}`)
    return res
  } catch (error) {
    return error
  }
}

export const editCategoryById = async (categoryId, { categoryName, eventDuration, eventCategoryDesc }) => {
  try {
    const res = await axios.put(`/api/categories/${categoryId}`, {
      categoryName,
      eventDuration,
      eventCategoryDesc,
    })
    return res
  } catch (error) {
    return error
  }
}
