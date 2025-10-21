<template>
  <br>
  <h1>Hallo, wähle deine Bahn</h1>
  <BRow>
    <BCol></BCol>
    <BCol lg="10">
      <the-pool :lane-count="laneStore.laneCount" counter-position="left" :reverse-lanes="true"></the-pool>
    </BCol>
    <BCol></BCol>
  </BRow>
</template>

<script>
import ThePool from "../components/UI/ThePool.vue";
import { useLaneStore } from "../store/index.js";

export default {
  name: "LaneSelect",
  components: {ThePool},
  setup() {
    const laneStore = useLaneStore();
    return {laneStore};
  },
  data() {
    return {
      width: window.innerWidth,
    };
  },
  computed: {
  },
  created() {
    window.addEventListener('resize', this.updateWidth);
  },
  destroyed() {
    window.removeEventListener('resize', this.updateWidth);
  },
  methods: {
    updateWidth() {
      this.width = window.innerWidth;
    }
  }
}
</script>

<style scoped>
h1 {
  text-align: center;
  color: #013157;
  margin-bottom: 1em;
}
</style>