<script setup>
// { categoryId: 'custom class' }
const categoryStyleClass = {
  1: 'project-management',
  2: 'dev-ops',
  3: 'database',
  4: 'client-side',
  5: 'server-side',
}

defineProps({
  category: {
    type: Object,
  },
})
</script>

<template>
  <div v-if="category" :class="`event-category ${categoryStyleClass[category.categoryId]}`">
    {{ category.categoryName }}
  </div>
</template>

<style lang="scss" scoped>
.event-category {
  @apply font-bold w-fit py-1 px-5 rounded-full text-xs;
}
.event-category.dev-ops {
  background: rgb(105, 237, 255);
  color: rgb(69, 112, 117);
}
.event-category.database {
  background: rgb(255, 99, 99);
  color: rgb(104, 47, 47);
}
.event-category.client-side {
  background: rgb(255, 227, 102);
  color: rgb(104, 105, 47);
}
.event-category.server-side {
  background: rgb(255, 137, 249);
  color: rgb(109, 57, 116);
}
.event-category.project-management {
  background: lightgreen;
  color: green;
}
</style>
