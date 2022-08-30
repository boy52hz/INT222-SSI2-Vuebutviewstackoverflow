import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useUsers = defineStore('users', () => {
  const users = ref([])

  // logiData => email (trimed), password(trimed)
  const loginUser = async (loginData) => {
    console.log(loginData)
  }

  const registerUser = async (registerData) => {
    const res = await fetch(`${import.meta.env.VITE_BASE_PATH}/api/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(registerData),
    })
    const data = await res.json()
    if (res.status === 201) {
      await fetchUsers()
      return data
    } else if (res.status === 400) {
      throw data
    }
  }

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
    deleteUserById,
    loginUser,
    registerUser,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot))
}
