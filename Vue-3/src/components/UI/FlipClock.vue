<template>
  <div class="flip-clock">
    <div class="flip-clock-card" v-for="(digit, index) in formattedKilometers" :key="index">
      <div class="flip-clock-card-front">{{ digit }}</div>
      <div class="flip-clock-card-back">{{ digit }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Number,
      required: true
    }
  },
  computed: {
    formattedKilometers() {
      return this.value.toFixed(3).split('');
    }
  }
};
</script>

<style scoped>
.flip-clock {
  display: flex;
  font-size: 2em;
  font-family: 'Courier New', Courier, monospace;
}

.flip-clock-card {
  position: relative;
  width: 1em;
  height: 1.5em;
  margin: 0 0.1em;
  perspective: 1000px;
}

.flip-clock-card-front,
.flip-clock-card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  transition: transform 0.6s;
}

.flip-clock-card-front {
  background: #fff;
  color: #000;
  transform: rotateX(0deg);
}

.flip-clock-card-back {
  background: #000;
  color: #fff;
  transform: rotateX(180deg);
}
</style>