<script setup></script>

<template>
  <div class="relative overflow-auto h-full">
    <table class="w-full text-sm">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 drop-shadow sticky top-0 w-full z-50">
        <tr>
          <slot name="head" />
        </tr>
      </thead>
      <tbody>
        <slot name="body" />
      </tbody>
    </table>
  </div>
</template>

<style scoped></style>
