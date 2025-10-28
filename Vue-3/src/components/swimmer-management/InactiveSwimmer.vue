<template>
  <v-card
      class="inactive-swimmer"
      @click="handleInactiveSwimmerClicked"
      @mouseover="handleMouseHover"
      @mouseleave="handleMouseLeave">
    <v-row class="p-3">
    <a class="swimmer-name">
      {{ swimmer.id }} -
    {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }}
    ({{ swimmer.age }}J)
    </a>
    </v-row>
    <v-row class="text-left p-3 pt-0" v-if="!hideInfoDefault || isInScope">
      <v-col class="swimmer-info">
        <v-chip :color="swimmer.swimDistance > 1000 ? 'primary' : 'info'"
                :class="{ 'font-italic': swimmer.swimDistance <= 1000 }"
                size="x-large">
          {{ formatDistance(swimmer.swimDistance) }}
        </v-chip>
      </v-col>
      <v-col class="swimmer-info">
        <v-chip color="primary" size="x-large">
          {{ formatLaneCount(swimmer.swimDistance) }}
        </v-chip>
      </v-col>
      <v-col v-if="swimmer.team !== ''" class="swimmer-info">
        <v-chip color="primary" size="x-large">
          {{ swimmer.team }}
          <v-icon class="ml-2" icon="mdi-account-group"></v-icon>
        </v-chip>
      </v-col>
      <v-col v-else-if="swimmer.family !== ''" class="swimmer-info">
        <v-chip color="primary" size="x-large">
          {{ swimmer.family }}
          <v-icon class="ml-2">mdi-home</v-icon>
        </v-chip>
      </v-col>
    </v-row>
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
  background-color: #f0f0f0;
  border: 3px solid gray;
  padding: 10px;
  margin: 5px;
  border-radius: 8px;
  cursor: pointer;
}

.swimmer-name {
  font-size: 1.5em;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
}

.swimmer-info {
  padding-left: 4px;
  padding-right: 4px;
}

.font-italic {
  font-style: italic;
}
</style>