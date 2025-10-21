<template>
  <div>
    <nav>
      <router-link :to="'/results'">RESULTS</router-link>
      <router-link :to="'/lane'">BAHNEN</router-link>
      <router-link :to="'/chekin'">CHECKIN</router-link>
    </nav>
    <main>
      <RouterView/>
    </main>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style scoped>
</style>
