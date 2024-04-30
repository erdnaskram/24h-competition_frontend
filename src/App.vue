<script setup>
</script>

<template>
  <nav>
    <router-link :to="'/results'">RESULTS</router-link>
    <router-link :to="'/lane'">BAHNEN</router-link>
    <router-link :to="'/chekin'">CHECKIN</router-link>
  </nav>
  <main>
    <RouterView/>
  </main>
</template>

<style scoped>
</style>
