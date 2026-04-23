<template>
  <v-dialog
      :model-value="modelValue"
      max-width="500"
      :fullscreen="isSmallScreen"
      @update:model-value="$emit('update:modelValue', $event)"
  >
    <v-card>
      <v-card-title class="text-wrap pt-4 px-4 text-h6" style="color: #013157">
        <div v-if="selectedSwimmer">
          {{ selectedSwimmer.firstName }} {{ selectedSwimmer.lastName }}
        </div>
        <div v-if="selectedSwimmer" class="assignment-swimmer-characteristics text-caption font-weight-regular text-medium-emphasis">
          Ø {{ formatTime(selectedSwimmer.averageTimeSeconds) }} &nbsp;·&nbsp;
          {{ selectedSwimmer.lanes }} Bahnen
        </div>
      </v-card-title>

      <v-card-text class="px-4">

        <!-- Empfohlene Bahn -->
        <div v-if="recommendedLane">
          <div class="text-caption text-medium-emphasis mb-1">
            <v-icon size="14" color="success">mdi-lightbulb-outline</v-icon>
            Empfehlung basiert auf Geschwindigkeit & Auslastung
          </div>
          <v-btn
              :style="{ backgroundColor: getLaneColor(recommendedLane.participants) }"
              size="x-large"
              variant="flat"
              block
              class="mb-3 white-text recommended-btn"
              @click="$emit('assign', recommendedLane.id)"
          >
            Bahn {{ recommendedLane.id }} –
            <span v-if="isCurrentLane(recommendedLane.id)">&nbsp;Empfohlen &amp; schon hier</span>
            <span v-else>&nbsp;Empfohlen</span>
          </v-btn>
          <div class="d-flex align-center mb-3">
            <v-divider />
            <span class="text-caption text-medium-emphasis px-3 text-no-wrap">alle Bahnen</span>
            <v-divider />
          </div>
        </div>

        <!-- Alle Bahnen -->
        <div v-for="lane in lanes" :key="lane.id" class="mb-2">
          <div v-if="isCurrentLane(lane.id)" class="current-lane-label">
            <v-icon size="13">mdi-map-marker</v-icon>
            Aktuell hier eingeteilt
          </div>
          <v-btn
              :style="{ backgroundColor: getLaneColor(lane.participants) }"
              size="x-large"
              variant="flat"
              block
              class="white-text lane-btn"
              :class="{ 'current-lane-btn': isCurrentLane(lane.id) }"
              @click="$emit('assign', lane.id)"
          >
            <v-row no-gutters align="center" justify="space-between" style="width:100%; gap: 12px">
              <v-col class="text-left lane-btn-name">
                <v-icon v-if="isCurrentLane(lane.id)" size="18" class="mr-1">mdi-map-marker</v-icon>
                <v-icon v-if="recommendedLane && recommendedLane.id === lane.id" size="18" class="mr-1">mdi-star</v-icon>
                Bahn {{ lane.id }}
              </v-col>
              <v-col class="text-right lane-btn-info">
                {{ lane.participants }} Schwimmer · Ø {{ formatTime(lane.averageTimeSeconds) }}
              </v-col>
            </v-row>
          </v-btn>
        </div>

      </v-card-text>

      <v-card-actions class="pb-4 px-4">
        <v-btn
            v-if="selectedSwimmer && selectedSwimmer.lane"
            color="error"
            variant="tonal"
            prepend-icon="mdi-exit-run"
            @click="$emit('remove')"
        >
          Von Bahn entfernen
        </v-btn>
        <v-spacer />
        <v-btn color="grey-darken-2" @click="$emit('update:modelValue', false)">Abbrechen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'CheckInAssignDialog',
  props: {
    modelValue:      { type: Boolean, required: true },
    selectedSwimmer: { type: Object,  default: null },
    recommendedLane: { type: Object,  default: null },
    lanes:           { type: Array,   required: true },
    laneColourYellow:{ type: Number,  required: true },
    laneColourRed:   { type: Number,  required: true },
    isSmallScreen:   { type: Boolean, default: false },
  },
  emits: ['update:modelValue', 'assign', 'remove'],
  methods: {
    isCurrentLane(laneId) {
      return !!(this.selectedSwimmer && this.selectedSwimmer.lane === laneId);
    },
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
.white-text { color: white !important; }

.assignment-swimmer-characteristics {
  font-weight: bold !important;
  font-size: 0.85rem !important;
}

.recommended-btn {
  box-shadow: 0 0 0 3px rgba(0,0,0,0.18), 0 4px 14px rgba(0,0,0,0.22) !important;
  font-weight: bold !important;
  font-size: 1.1rem !important;
}

.lane-btn { font-size: 1rem !important; }
.lane-btn-name { font-size: 1rem; font-weight: bold; }
.lane-btn-info { font-size: 0.85rem; font-weight: normal; }

.current-lane-label {
  font-size: 0.72rem;
  font-weight: 600;
  color: #013157;
  background: rgba(1, 49, 87, 0.08);
  border-radius: 4px 4px 0 0;
  padding: 2px 10px;
  display: flex;
  align-items: center;
  gap: 3px;
}

.current-lane-btn {
  box-shadow: inset 0 0 0 4px rgba(255,255,255,0.85) !important;
  font-weight: bold !important;
  border-radius: 0 0 4px 4px !important;
}
</style>
