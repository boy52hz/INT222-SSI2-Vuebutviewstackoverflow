<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CategoryEditForm from '../components/Category/CategoryEditForm.vue'
import * as categoriesApi from '../apis/categories'
import { useToast } from 'vue-toastification'

const toast = useToast()
const route = useRoute()
const router = useRouter()

const isLoading = ref(false)
const isFetching = ref(true)
const editCategoryForm = ref({})

const handleSaveCategory = async ({ categoryId, categoryName, eventDuration, eventCategoryDesc }) => {
  isLoading.value = true
  const { data, error } = await categoriesApi.editCategoryById(categoryId, {
    categoryName,
    eventDuration,
    eventCategoryDesc,
  })
  if (error) {
    isLoading.value = false
    toast.error(Object.values(error?.fieldErrors).join(', ') || error?.message)
    return
  }
  toast.success('Saved')
  router.replace('/categories')
}

const fetchCategoryById = async (categoryId) => {
  isFetching.value = true
  const { data: category, error } = await categoriesApi.getCategoryById(categoryId)
  isFetching.value = false
  if (error) {
    router.replace('/')
    return
  }
  editCategoryForm.value = {
    categoryId: category.categoryId,
    categoryName: category.categoryName,
    eventCategoryDesc: category.eventCategoryDesc,
    eventDuration: category.eventDuration,
  }
}

watch(
  () => route.params,
  () => {
    fetchCategoryById(route.params.categoryId)
  },
  { immediate: true }
)
</script>

<template>
  <div class="container max-w-lg mx-auto space-y-3">
    <div class="font-bold text-xl">Edit Category - {{ editCategoryForm?.categoryName }}</div>
    <CategoryEditForm :category-model="editCategoryForm" @save="handleSaveCategory" :is-loading="isLoading" />
  </div>
</template>

<style></style>
