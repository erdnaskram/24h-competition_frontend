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
        style="background-color: white;"
        hover
        @click="$emit('select', swimmer)"
    >
      <v-card-text class="pa-3">
        <v-row align="center" no-gutters>
          <v-col>
            <div class="swimmer-name">
              {{ swimmer.id }} – {{ swimmer.firstName }} {{ swimmer.lastName }}
              ({{ swimmer.age }}J, {{ swimmer.gender === 'm' ? '♂' : '♀' }})
            </div>
            <div class="text-body-2 text-medium-emphasis mt-1">
              <v-icon size="14">mdi-timer-outline</v-icon>
              {{ formatTime(swimmer.averageTimeSeconds) }}
              &nbsp;&bull;&nbsp;
              <v-icon size="14">mdi-swim</v-icon>
              {{ swimmer.lanes }} Bahnen / {{ (swimmer.lanes * 25).toLocaleString('de-DE') }} m
              <span v-if="swimmer.lane" class="font-weight-bold ml-2">
                · {{ swimmer.isActive ? 'Schwimmt auf' : 'Eingeteilt auf' }} Bahn {{ swimmer.lane }}
              </span>
            </div>
          </v-col>
          <v-col cols="auto">
            <v-icon color="grey-lighten-1">mdi-chevron-right</v-icon>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'CheckInSwimmerList',
  props: {
    swimmers: { type: Array, required: true },
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
