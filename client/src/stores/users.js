import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useUsers = defineStore('users', () => {
  const users = ref([])

  // logiData => email (trimed), password(trimed)
  const loginUser = async (loginData) => {
    console.log(loginData)
  }

  const registerUser = async (registerData) => {}

  const fetchUsers = async () => {
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_PATH}/api/users`)
      if (res.status === 200) {
        const data = await res.json()
        users.value = data
      }
    } catch (err) {
      console.log(err)
    }
  }

  const fetchUserByUserId = async (userId) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_PATH}/api/users/${userId}`)
    const data = await res.json()
    if (res.status === 200) {
      return data
    } else {
      throw data
    }
  }

  const deleteUserById = async (userId) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_PATH}/api/users/${userId}`, {
      method: 'DELETE',
    })
    if (res.status === 200) {
      await fetchUsers()
    } else {
      const data = await res.json()
      throw data
    }
  }

  return {
    users,
    fetchUsers,
    fetchUserByUserId,
    deleteUserById,
    loginUser,
    registerUser,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot))
}
