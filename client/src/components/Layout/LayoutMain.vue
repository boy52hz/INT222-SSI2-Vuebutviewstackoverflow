<script setup>
import LayoutHeader from './LayoutHeader.vue'
import Sidebar from '../Sidebar.vue'
</script>

<template>
  <div class="flex flex-col h-full lg:flex-row">
    <Sidebar class="lg:flex-[1]" />
    <div class="lg:flex-[7] flex flex-col h-full w-full">
      <LayoutHeader />
      <div class="p-4 lg:p-5 h-full overflow-y-auto">
        <slot />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
