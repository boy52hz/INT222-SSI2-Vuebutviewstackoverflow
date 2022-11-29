<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="flex flex-col justify-center items-center w-full h-full space-y-5">
    <h1 class="text-4xl text-gray-600 font-light">Something went wrong :(</h1>
    <RouterLink class="underline underline-offset-2 text-gray-800" to="/">Back to homepage</RouterLink>
  </div>
</template>

<style></style>
