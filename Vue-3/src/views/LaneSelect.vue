<template>
  <v-container class="py-6">
    <v-row justify="center">
      <v-col cols="12" class="text-center">
        <h1>Hallo, wähle deine Bahn</h1>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col cols="12" lg="11" xl="8">
        <the-pool
            :lane-count="laneStore.laneCount"
            counter-position="left"
            :reverse-lanes="true"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import ThePool from "../components/UI/ThePool.vue";
import {useLaneStore} from "../store/index.js";

export default {
  name: "LaneSelect",
  components: {ThePool},
  setup() {
    const laneStore = useLaneStore();
    return {laneStore};
  },
}
</script>

<style scoped>
h1 {
  color: #013157;
  font-size: clamp(1.4rem, 4vw, 2.2rem);
  font-weight: bold;
}
</style>
