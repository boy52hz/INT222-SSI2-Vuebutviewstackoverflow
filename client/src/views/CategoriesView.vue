<script setup>
import ListBox from '../components/ListBox/ListBox.vue'
import ListBoxBody from '../components/ListBox/ListBoxBody.vue'
import ListBoxHeader from '../components/ListBox/ListBoxHeader.vue'
import Card from '../components/Card.vue'
import { ref } from 'vue'
import { useAuthenticationStore } from '../stores/authentication'
import * as categoriesApi from '../apis/categories'
import ScheduleCategoryBadge from '../components/Schedule/ScheduleCategoryBadge.vue'
import { Role } from '../enums/Role'
import { useRouter } from 'vue-router'

const authStore = useAuthenticationStore()

const router = useRouter()
const isFetching = ref(true)
const categories = ref([])

const handleEditCategoryClick = (categoryId) => {
  router.push(`/categories/${categoryId}`)
}

const fetchCategories = async () => {
  isFetching.value = true
  const { data, error } = await categoriesApi.getCategories()
  categories.value = data
  isFetching.value = false
}

fetchCategories()
</script>

<template>
  <div class="w-full h-full">
    <ListBox>
      <ListBoxHeader>
        <div class="font-bold text-sm lg:text-base">{{ categories?.length || 0 }} Categories</div>
      </ListBoxHeader>
      <ListBoxBody v-if="isFetching">
        <div class="h-fit grid grid-cols-1 lg:grid-cols-2 gap-3">
          <Card v-for="index in 5" class="opacity-30" :key="index" :skeleton="true" />
        </div>
      </ListBoxBody>
      <ListBoxBody v-else>
        <div class="h-fit grid grid-cols-1 lg:grid-cols-2 gap-3">
          <Card v-for="category in categories" :key="category.categoryId" class="flex justify-between">
            <div class="space-y-3">
              <ScheduleCategoryBadge class="!font-bold !text-sm !lg:text-xl !xl:text-2xl" :category="category" />
              <div class="space-x-1 lg:space-x-2 text-xs lg:text-sm text-blue-900">
                <font-awesome-icon icon="fa-solid fa-clock" /><span>{{ category.eventDuration }} Minutes</span>
              </div>
              <div class="space-x-1 lg:space-x-2 text-xs lg:text-sm mt-3 italic">
                {{ category?.eventCategoryDesc || 'No description' }}
              </div>
            </div>
            <div
              v-if="[Role.Admin].includes(authStore.user?.role)"
              class="text-amber-500 hover:text-amber-600 hover:cursor-pointer transition-all duration-300"
              @click="handleEditCategoryClick(category.categoryId)"
            >
              <font-awesome-icon icon="fa-regular fa-pen-to-square" />
            </div>
          </Card>
        </div>
      </ListBoxBody>
    </ListBox>
  </div>
</template>

<style scoped></style>
