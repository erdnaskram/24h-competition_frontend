<template>
  <v-card
      class="inactive-swimmer"
      @click="handleInactiveSwimmerClicked"
      @mouseover="handleMouseHover"
      @mouseleave="handleMouseLeave">
    <v-row class="pa-3">
    <a class="swimmer-name" :class="{ 'swimmer-name--truncate': !isInScope }">
      {{ swimmer.id }} - {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }} ({{ swimmer.age }}J)
    </a>
    </v-row>
    <div class="chip-row" v-if="!hideInfoDefault || isInScope">
      <v-chip :color="swimmer.swimDistance > 1000 ? 'primary' : 'info'"
              :class="{ 'font-italic': swimmer.swimDistance <= 1000 }"
              size="default">
        {{ formatDistance(swimmer.swimDistance) }}
      </v-chip>
      <v-chip color="primary" size="default">
        {{ formatLaneCount(swimmer.swimDistance) }}
      </v-chip>
      <v-chip v-if="swimmer.team !== ''" color="primary" size="default">
        <v-icon start icon="mdi-account-group" />
        {{ swimmer.team }}
      </v-chip>
      <v-chip v-else-if="swimmer.family !== ''" color="primary" size="default">
        <v-icon start>mdi-home</v-icon>
        {{ swimmer.family }}
      </v-chip>
    </div>
  </v-card>
</template>

<script>
export default {
  name: "InactiveSwimmer",
  props: {
    swimmer: {
      type: Object,
      required: true,
    },
    hideInfoDefault: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      isInScope: false,
    };
  },
  methods: {
    handleInactiveSwimmerClicked() {
      if (this.isInScope) {
        this.$emit('inactiveSwimmerClicked', this.swimmer);
      }
    },
    formatDistance(distance) {
      return distance > 1000 ? `${(distance / 1000).toLocaleString('de-DE')} km` : `${distance.toLocaleString('de-DE')} m`;
    },
    formatLaneCount(distance) {
      return `${distance / 25} Bahnen`
    },
    handleMouseHover() {
      this.isInScope = true;
    },
    handleMouseLeave() {
      this.isInScope = false;
    }
  },
}
</script>


<style scoped>
.inactive-swimmer {
  user-select: none;
  background-color: #f0f0f0;
  border: 3px solid gray;
  padding: 10px;
  margin: 5px;
  border-radius: 8px;
  cursor: pointer;
}

.swimmer-name {
  font-size: 1.2rem;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
  display: block;
  white-space: normal;
  overflow: visible;
  width: 100%;
}

.swimmer-name--truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chip-row {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  padding: 4px 0 2px 0;
}

.font-italic {
  font-style: italic;
}
</style>