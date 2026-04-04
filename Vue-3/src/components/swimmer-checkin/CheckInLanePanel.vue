<template>
  <div>
    <!-- Bahn-Karten -->
    <v-card
        v-for="lane in lanes"
        :key="lane.id"
        variant="flat"
        class="mb-2 lane-card"
        :style="{
          borderLeft: '5px solid ' + getLaneColor(lane.participants),
          outline: filterLane === lane.id ? '2px solid #013157' : 'none',
          backgroundColor: filterLane === lane.id ? '#e8f0f7' : 'white',
        }"
        hover
        style="cursor: pointer;"
        @click="$emit('update:filterLane', filterLane === lane.id ? null : lane.id)"
    >
      <v-card-text class="pa-3">
        <v-row align="center" no-gutters>
          <v-col>
            <div class="d-flex align-center ga-2">
              <div class="lane-card-title">Bahn {{ lane.id }}</div>
              <v-chip v-if="filterLane === lane.id" size="x-small" style="background-color: #013157 !important; color: white !important;">
                <v-icon start size="10">mdi-filter</v-icon>
                Gefiltert
              </v-chip>
            </div>
            <div class="text-body-2 text-medium-emphasis">
              Ø {{ formatTime(lane.averageTimeSeconds) }}
            </div>
          </v-col>
          <v-col cols="auto">
            <v-chip
                :style="{ backgroundColor: getLaneColor(lane.participants) + ' !important', minWidth: '48px', justifyContent: 'center' }"
                size="default"
                class="font-weight-bold"
                style="color: white !important;"
            >
              {{ lane.participants }}
              <v-icon end size="14">mdi-account</v-icon>
            </v-chip>
          </v-col>
        </v-row>
        <v-progress-linear
            :model-value="(lane.participants / laneColourRed) * 100"
            :color="getLaneColor(lane.participants)"
            height="4"
            rounded
            class="mt-2"
            bg-color="#e0e0e0"
        />
      </v-card-text>
    </v-card>

    <!-- Gesamtstrecke -->
    <v-card variant="flat" class="mt-3 text-center" style="background-color: white;">
      <v-card-text class="py-3">
        <div class="text-h4 font-weight-bold" style="color: #013157">
          {{ totalDistanceFormatted }}
        </div>
        <div class="text-caption text-medium-emphasis">Gesamtstrecke</div>
      </v-card-text>
    </v-card>

    <!-- Farbgrenzen (einklappbar) -->
    <v-card variant="flat" style="background-color: white;" class="mt-2">
      <v-card-text class="pa-2">
        <div
            class="d-flex align-center justify-space-between"
            style="cursor: pointer;"
            @click="showColorLimits = !showColorLimits"
        >
          <span class="text-caption text-medium-emphasis">Farbgrenzen</span>
          <v-icon size="18" color="grey">
            {{ showColorLimits ? 'mdi-chevron-up' : 'mdi-chevron-down' }}
          </v-icon>
        </div>
        <v-expand-transition>
          <div v-if="showColorLimits" class="mt-3">
            <v-row align="center" dense>
              <v-col cols="5" class="d-flex align-center ga-1">
                <v-icon color="warning" size="16">mdi-circle</v-icon>
                <span class="text-body-2">Gelb ab</span>
              </v-col>
              <v-col>
                <v-text-field
                    :model-value="laneColourYellow"
                    type="number"
                    variant="outlined"
                    density="compact"
                    hide-details
                    @update:model-value="$emit('update:laneColourYellow', Number($event))"
                />
              </v-col>
            </v-row>
            <v-row align="center" dense class="mt-2">
              <v-col cols="5" class="d-flex align-center ga-1">
                <v-icon color="error" size="16">mdi-circle</v-icon>
                <span class="text-body-2">Rot ab</span>
              </v-col>
              <v-col>
                <v-text-field
                    :model-value="laneColourRed"
                    type="number"
                    variant="outlined"
                    density="compact"
                    hide-details
                    @update:model-value="$emit('update:laneColourRed', Number($event))"
                />
              </v-col>
            </v-row>
          </div>
        </v-expand-transition>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'CheckInLanePanel',
  props: {
    lanes:                { type: Array,  required: true },
    laneColourYellow:     { type: Number, required: true },
    laneColourRed:        { type: Number, required: true },
    totalDistanceFormatted: { type: String, required: true },
    filterLane:           { type: Number, default: null },
  },
  emits: ['update:laneColourYellow', 'update:laneColourRed', 'update:filterLane'],
  data() {
    return { showColorLimits: false };
  },
  methods: {
    getLaneColor(participants) {
      if (participants >= this.laneColourRed)    return '#cc3232';
      if (participants >= this.laneColourYellow) return '#e7b416';
      return '#99c140';
    },
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
.lane-card { border-radius: 6px; overflow: hidden; }
.lane-card-title { font-size: 1.15rem; font-weight: bold; color: #013157; }
.white-text { color: white !important; }
</style>
