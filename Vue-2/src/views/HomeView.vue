<template>
  <section>
    <h2>HomeView</h2>
    <button @click="goToAbout">Go to About</button>
  </section>
</template>

<script>
export default {
  methods: {
    goToAbout() {
      this.$router.push('/about')
    },
  },
}
</script>

<style scoped>

</style>