<script setup>
import ListBox from '../components/ListBox/ListBox.vue'
import ListBoxBody from '../components/ListBox/ListBoxBody.vue'
import ListBoxHeader from '../components/ListBox/ListBoxHeader.vue'
import Card from '../components/Card.vue'
import { ref, watch } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'
import * as categoriesApi from '../apis/categories'
const authStore = useAuthenticationStore()

const isLoading = ref(true)
const categories = ref([])

const fetchCategories = async () => {
  isLoading.value = true
  const { data, error } = await categoriesApi.getCategories()
  categories.value = data
  isLoading.value = false
}

watch(
  () => authStore.isAuthenticated,
  (isAuthenticated) => {
    if (isAuthenticated) {
      fetchCategories()
    }
  }
)

fetchCategories().finally(() => (isLoading.value = false))
</script>

<template>
  <div class="w-full h-full">
    <ListBox>
      <ListBoxHeader>
        <div class="font-bold text-sm lg:text-base">{{ categories?.length || 0 }} Categories</div>
      </ListBoxHeader>
      <ListBoxBody v-if="isLoading">
        <Card v-for="index in 5" class="opacity-30" :key="index" :skeleton="true" />
      </ListBoxBody>
      <ListBoxBody v-else>
        <Card v-for="category in categories" :key="category.categoryId">
          <h1 class="font-bold text-sm lg:text-xl">{{ category.categoryName }}</h1>
          <div class="space-x-1 lg:space-x-2 text-xs lg:text-base">
            <font-awesome-icon icon="fa-solid fa-clock" /><span>{{ category.eventDuration }} Minutes</span>
          </div>
          <div class="space-x-1 lg:space-x-2 text-xs lg:text-sm mt-3 italic">
            {{ category?.eventCategoryDesc || 'No description' }}
          </div>
        </Card>
      </ListBoxBody>
    </ListBox>
  </div>
</template>

<style scoped></style>
