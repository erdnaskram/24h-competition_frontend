<template>
  <div>
    <div v-if="swimmers.length === 0" class="text-center text-medium-emphasis py-10">
      <v-icon size="52" class="mb-3">mdi-account-search</v-icon>
      <div class="text-body-1">Keine Schwimmer gefunden</div>
    </div>

    <v-card
        v-for="swimmer in swimmers"
        :key="swimmer.id"
        class="mb-2"
        variant="flat"
        :style="swimmer.lane ? 'background-color: #E8F5E9; border-left: 5px solid #4CAF50;' : 'background-color: white;'"
        hover
        @click="$emit('select', swimmer)"
    >
      <v-card-text class="pa-3">
        <v-row align="center" no-gutters>
          <v-col>
            <div class="swimmer-name">
              {{ swimmer.id }} – {{ swimmer.firstName }} {{ swimmer.lastName }}
              ({{ swimmer.age }}J, <gender-icon :gender="swimmer.gender" :size="16" />)
            </div>
            <div class="text-body-2 text-medium-emphasis mt-1">
              <v-icon size="14">mdi-timer-outline</v-icon>
              {{ formatTime(swimmer.averageTimeSeconds) }}
              &nbsp;&bull;&nbsp;
              <v-icon size="14">mdi-swim</v-icon>
              {{ swimmer.lanes }} Bahnen / {{ (swimmer.lanes * 25).toLocaleString('de-DE') }} m
              <span v-if="swimmer.lane" class="font-weight-bold d-block d-sm-inline" :style="{ color: swimmer.isActive ? '#2E7D32' : '#757575' }">
                <span class="d-none d-sm-inline">&nbsp;&bull;&nbsp;</span>{{ swimmer.isActive ? 'Schwimmt auf' : 'Eingeteilt auf' }} Bahn {{ swimmer.lane }}
              </span>
            </div>
          </v-col>
          <v-col cols="auto" class="d-flex flex-column align-center ga-1">
            <v-icon v-if="betterLaneSuggestions.has(swimmer.id)" size="18" title="Bessere Bahn verfügbar" style="color: #FB8C00;">mdi-swap-horizontal</v-icon>
            <v-icon color="grey-lighten-1">mdi-chevron-right</v-icon>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import GenderIcon from '../GenderIcon.vue';
export default {
  name: 'CheckInSwimmerList',
  components: { GenderIcon },
  props: {
    swimmers:             { type: Array,  required: true },
    betterLaneSuggestions: { type: Set,   default: () => new Set() },
  },
  emits: ['select'],
  methods: {
    formatTime(seconds) {
      if (!seconds) return '--:--';
      const min = Math.floor(Math.abs(seconds) / 60);
      const sec = Math.abs(seconds) % 60;
      return `${min}:${sec.toString().padStart(2, '0')}`;
    },
  },
};
</script>

<style scoped>
.swimmer-name {
  font-size: 1.2rem;
  font-weight: bold;
  color: #013157;
}
</style>
