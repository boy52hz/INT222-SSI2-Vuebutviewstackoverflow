<script setup>
import ListBox from '../components/ListBox/ListBox.vue'
import ListBoxBody from '../components/ListBox/ListBoxBody.vue'
import ListBoxHeader from '../components/ListBox/ListBoxHeader.vue'
import Card from '../components/Card.vue'
import { ref, watch } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'
import * as categoriesApi from '../apis/categories'
import ScheduleCategoryBadge from '../components/Schedule/ScheduleCategoryBadge.vue'
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
        <div class="h-fit grid grid-cols-1 lg:grid-cols-2 gap-3">
          <Card v-for="index in 5" class="opacity-30" :key="index" :skeleton="true" />
        </div>
      </ListBoxBody>
      <ListBoxBody v-else>
        <div class="h-fit grid grid-cols-1 lg:grid-cols-2 gap-3">
          <Card v-for="category in categories" :key="category.categoryId" class="space-y-3 h-fit">
            <ScheduleCategoryBadge class="!font-bold !text-sm !lg:text-xl !xl:text-2xl" :category="category" />
            <div class="space-x-1 lg:space-x-2 text-xs lg:text-sm text-blue-900">
              <font-awesome-icon icon="fa-solid fa-clock" /><span>{{ category.eventDuration }} Minutes</span>
            </div>
            <div class="space-x-1 lg:space-x-2 text-xs lg:text-sm mt-3 italic">
              {{ category?.eventCategoryDesc || 'No description' }}
            </div>
          </Card>
        </div>
      </ListBoxBody>
    </ListBox>
  </div>
</template>

<style scoped></style>
