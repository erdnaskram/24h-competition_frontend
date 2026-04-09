<template>
  <v-app :style="{ backgroundImage: `url(${isDark ? darkBg : lightBg})` }">
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
import {useDarkMode} from './composables/useDarkMode.js';
import lightBg from './assets/Background.png';
import darkBg from './assets/Background_Darkmode.png';

export default {
  name: 'App',
  components: {TheHeader},
  setup() {
    const route = useRoute();
    const {isDark} = useDarkMode();
    return {route, isDark, lightBg, darkBg};
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
  height: 48px;
}
.v-application {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
}
</style>