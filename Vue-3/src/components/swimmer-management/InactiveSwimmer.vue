<template>
  <BRow class="inactive-swimmer" @mouseover="!hideInfoDefault && (isHovered = true)"
        @mouseleave="!hideInfoDefault && (isHovered = false)"><a class="swimmer-name">{{ swimmer.id }} -
    {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }}
    ({{ swimmer.age }})</a>
    <v-row class="text-left" v-if="!hideInfoDefault && isHovered">
      <v-col sm="4" class="swimmer-info">
        <v-chip :color="distance > 1000 ? 'primary' : 'primary'"
                :class="{ 'font-italic': swimmer.swimDistance <= 1000 }"
                size="x-large">
          {{ formatDistance(swimmer.swimDistance) }}
        </v-chip>
      </v-col>
      <v-col sm="5" class="swimmer-info">
        <v-chip color="primary" size="x-large">
          {{ formatLaneCount(swimmer.swimDistance) }}
        </v-chip>
      </v-col>
      <v-col v-if="swimmer.team !== ''" sm="3" class="swimmer-info">
        <v-chip color="primary" size="x-large">
          {{ swimmer.team }}
          <v-icon icon="mdi-account-group"></v-icon>
        </v-chip>
      </v-col>
      <v-col v-else-if="swimmer.family !== ''" sm="3" class="swimmer-info">
        <v-chip color="primary" size="x-large">
          {{ swimmer.family }}
          <v-icon>mdi-home</v-icon>
        </v-chip>
      </v-col>
    </v-row>


  </BRow>
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
      isHovered: false,
    };
  },
  methods: {
    // Add any methods you need here
    formatDistance(distance) {
      return distance > 1000 ? `${(distance / 1000).toLocaleString('de-DE')} km` : `${distance.toLocaleString('de-DE')} m`;
    },
    formatLaneCount(distance) {
      return `${distance / 25} Bahnen`
    },
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
}

.swimmer-name {
  font-size: 1.7em;
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