<template>
  <v-card
      class="inactive-swimmer"
      @click="handleInactiveSwimmerClicked"
      @mouseover="handleMouseHover"
      @mouseleave="handleMouseLeave">
    <v-row class="pa-3">
    <a class="swimmer-name" :class="{ 'swimmer-name--truncate': !isInScope }">
      {{ swimmer.id }} - {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }} ({{ swimmer.age }}J, <gender-icon :gender="swimmer.gender" :size="16" />)
    </a>
    </v-row>
    <div v-if="!hideInfoDefault || isInScope" class="text-body-2 text-medium-emphasis mt-1">
      <v-icon size="14">mdi-swim</v-icon>
      {{ swimmer.swimDistance / 25 }} Bahnen / {{ formatDistance(swimmer.swimDistance) }}
    </div>
  </v-card>
</template>

<script>
import GenderIcon from '../GenderIcon.vue';
export default {
  name: "InactiveSwimmer",
  components: { GenderIcon },
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

</style>