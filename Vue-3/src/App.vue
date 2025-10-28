<template>
  <v-app>
    <the-header v-if="showHeader" :lane-count="laneCount"></the-header>
    <div>
      <div class="header-placeholder"></div>
      <main class="main-content">
        <RouterView/>
      </main>
    </div>
  </v-app>
</template>

<script>
import TheHeader from "./components/UI/TheHeader.vue";
import {useRoute} from 'vue-router';

export default {
  name: 'App',
  components: {TheHeader},
  setup() {
    const route = useRoute();
    return {route};
  },
  data() {
    return {
      width: window.innerWidth,
      laneCount: 5,
    };
  },

  computed: {
    showHeader() {
      const path = this.route.path;

      console.log(/\/lane\/\d+$/.test(path))
      return !path.endsWith('/results') && !/\/lane\/\d+$/.test(path);
    }
  },
}
</script>

<style scoped>
.header-placeholder {
  height: 80px;
}
.v-application {
  background-image: url('assets/Background.png'); /* Pfad zu Ihrem Hintergrundbild */
  background-size: cover; /* Bildgröße an den Bildschirm anpassen */
  background-position: center; /* Bild zentrieren */
  background-repeat: no-repeat; /* Bild nicht wiederholen */
  background-attachment: fixed; /* Hintergrundbild fixieren */
}
</style>