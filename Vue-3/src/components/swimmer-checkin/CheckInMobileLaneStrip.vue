<template>
  <div class="d-flex lane-strip">
    <div
        v-for="lane in lanes"
        :key="lane.id"
        class="lane-strip-item"
        :style="{ borderTop: '3px solid ' + getLaneColor(lane.participants), backgroundColor: filterLane.includes(lane.id) ? '#e8f0f7' : 'white' }"
        @click="$emit('update:filterLane', filterLane.includes(lane.id) ? filterLane.filter(id => id !== lane.id) : [...filterLane, lane.id])"
        @touchstart.passive="startLongPress"
        @touchend.passive="cancelLongPress"
        @touchmove.passive="cancelLongPress"
    >
      <div class="lane-strip-title">Bahn {{ lane.id }}</div>
      <div class="lane-strip-count" :style="{ color: getLaneColor(lane.participants) }">{{ lane.participants }}</div>
      <div v-if="lane.activeParticipants > 0" class="lane-strip-active">{{ lane.activeParticipants }} aktiv</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CheckInMobileLaneStrip',
  props: {
    lanes:            { type: Array,  required: true },
    laneColourYellow: { type: Number, required: true },
    laneColourRed:    { type: Number, required: true },
    filterLane:       { type: Array,  default: () => [] },
  },
  emits: ['update:filterLane', 'longpress'],
  data() {
    return { longPressTimer: null };
  },
  methods: {
    getLaneColor(participants) {
      if (participants >= this.laneColourRed)    return '#F44336';
      if (participants >= this.laneColourYellow) return '#FFC107';
      return '#8BC34A';
    },
    startLongPress() {
      this.longPressTimer = setTimeout(() => {
        this.$emit('longpress');
      }, 600);
    },
    cancelLongPress() {
      clearTimeout(this.longPressTimer);
    },
  },
};
</script>

<style scoped>
.lane-strip {
  gap: 6px;
  overflow-x: auto;
}

.lane-strip-item {
  flex: 1;
  min-width: 60px;
  border-radius: 6px;
  padding: 6px 8px;
  text-align: center;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.lane-strip-title {
  font-size: 0.75rem;
  font-weight: bold;
  color: #013157;
}

.lane-strip-count {
  font-size: 1.2rem;
  font-weight: bold;
  line-height: 1.2;
}

.lane-strip-active {
  font-size: 0.65rem;
  font-weight: bold;
  color: #2E7D32;
  line-height: 1.2;
}
</style>
