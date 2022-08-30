import { cat } from 'fontawesome'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'
import router from '../router'

export const useUsers = defineStore('users', () => {
  const users = ref([])

  // logiData => email (trimed), password(trimed)
  const loginUser = async (loginData) => {
      const res = await fetch(`${import.meta.env.VITE_BASE_PATH}/api/users/match`, {
        method:'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body:JSON.stringify(loginData),
      })
      const data = await res.json()
      if (res.status === 200) {
        router.push(`${import.meta.env.VITE_BASE_PATH}/users/`)
        await fetchUsers()
        return data
      } else if (res.status === 400) {
        throw data
      }
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
      router.push(`${import.meta.env.VITE_BASE_PATH}/match/`)
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
