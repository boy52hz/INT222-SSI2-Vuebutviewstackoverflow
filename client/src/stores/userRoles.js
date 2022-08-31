import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useUserRoles = defineStore('userRoles', () => {
  const userRoles = ref([])

  const fetchUserRoles = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_PATH}/api/roles`)
    if (res.status === 200) {
      userRoles.value = await res.json()
    }
  }

  const toOptions = () => {
    const options = []
    userRoles.value.forEach((role) => {
      options.push({
        label: role.label,
        value: role.name,
      })
    })
    return options
  }

  return {
    userRoles,
    fetchUserRoles,
    toOptions,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUserRoles, import.meta.hot))
}
