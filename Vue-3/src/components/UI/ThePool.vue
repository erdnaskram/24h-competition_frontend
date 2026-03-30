<template>
  <BRow class="pool-container flex-nowrap" no-gutters>
    <BCol cols="1" v-if="counterPosition === 'left'" class="counter counter-left">Zähler</BCol>
    <BCol cols="10" class="pool">
      <div v-for="lane in lanes" :key="lane"  class="lane-intersection">
        <div class="lane">
          <router-link :to="'/lane/' + lane">Bahn {{ lane }}</router-link>
        </div>
      </div>
    </BCol>
    <BCol cols="1" v-if="counterPosition === 'right'" class="counter counter-right">Zähler</BCol>
  </BRow>
</template>

<script>
export default {
  name: 'ThePool',
  props: {
    laneCount: {
      type: Number,
      required: true
    },
    counterPosition: {
      type: String,
      required: true
    },
    reverseLanes: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    lanes() {
      const lanesArray = Array.from({ length: this.laneCount }, (_, i) => i + 1);
      return this.reverseLanes ? lanesArray.reverse() : lanesArray;
    }
  }
}
</script>

<style scoped>
.pool-container {
  display: flex;
  align-items: stretch;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
  padding: 0.2rem;
  margin: 2rem auto;
  background: gray;
}

.pool {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #72ABD8;
  padding: 0;
  border: 10px solid gray;
  position: relative;
  flex-grow: 1;
}

.lane-intersection {
  width: 100%;
  height: 80px;
  text-align: center;
  margin: 0;
  padding: 0;
  border: 0;
  position: relative;
}

.lane-intersection:not(:last-child)::after {
  content: '';
  display: block;
  width: 100%;
  height: 10px;
  background: repeating-linear-gradient(
      90deg,
      red,
      red 10px,
      white 10px,
      white 20px
  );
  position: absolute;
  bottom: -5px;
  left: 0;
}

.lane {
  width: 100%;
  text-align: center;
  background-color: #72ABD8;
  padding: 10px;
  position: relative;
}

.lane a {
  text-decoration: none;
  color: #013157;
  font-weight: bold;
  font-size: 2em;
}

.counter {
  writing-mode: vertical-rl;
  text-orientation: mixed;
  text-decoration: none;
  color: #013157;
  font-weight: bold;
  font-size: 2em;
  transform: rotate(180deg);

  background-color: #ffb9b9;
  padding: 10px;
  border-top: 10px solid gray;
  border-bottom: 10px solid gray;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-grow: 1;
}
.counter-left {
  border-right: 10px solid gray;
}
.counter-right {
  border-left: 10px solid gray;
}
</style>