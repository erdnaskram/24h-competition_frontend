<template>
  <v-container fluid class="py-4 checkin-container">
    <v-row class="checkin-row">

      <!-- ── Links: Suche + Keyboard + Schwimmerliste ── -->
      <v-col cols="12" md="7" lg="9" class="left-col">

        <div class="search-area" style="background-color: white; border-radius: 8px; padding: 12px; margin-bottom: 8px; box-shadow: 0 1px 4px rgba(0,0,0,0.08);">
          <CheckInSearchBar
              v-model:search="searchQuery"
              v-model:showKeyboard="showKeyboard"
              v-model:filterLane="filterLane"
              v-model:sortBy="sortBy"
              v-model:sortAsc="sortAsc"
              v-model:statusFilter="statusFilter"
              :alphabet="alphabet"
              :sortOptions="sortOptions"
              :lanes="lanesWithCounts"
              @clear="clearFilter"
          />
        </div>

        <!-- Kompakte Bahnanzeige (nur Mobile) -->
        <div class="mobile-top d-md-none mb-3">
          <CheckInMobileLaneStrip
              :lanes="lanesWithCounts"
              :laneColourYellow="laneColourYellow"
              :laneColourRed="laneColourRed"
              v-model:filterLane="filterLane"
              @longpress="showColorLimitsMobile = true"
          />
        </div>

        <div class="swimmer-scroll">
          <CheckInSwimmerList
              :swimmers="filteredSwimmers"
              :betterLaneSuggestions="betterLaneSuggestions"
              @select="openLaneDialog"
          />
        </div>

      </v-col>

      <!-- ── Rechts: Bahn-Übersicht + Einstellungen (nur ab md) ── -->
      <v-col class="d-none d-md-block right-col" md="5" lg="3">
        <CheckInLanePanel
            :lanes="lanesWithCounts"
            v-model:laneColourYellow="laneColourYellow"
            v-model:laneColourRed="laneColourRed"
            :totalDistanceFormatted="totalDistanceFormatted"
            v-model:filterLane="filterLane"
        />
      </v-col>

    </v-row>

    <!-- ── Farbgrenzen Dialog (Mobile Long-Press) ── -->
    <v-dialog v-model="showColorLimitsMobile" max-width="320">
      <v-card>
        <v-card-title class="pt-4 px-4 text-body-1 font-weight-bold" style="color:#013157">Farbgrenzen</v-card-title>
        <v-card-text class="px-4">
          <v-row align="center" dense class="mb-2">
            <v-col cols="5" class="d-flex align-center ga-1">
              <v-icon color="warning" size="16">mdi-circle</v-icon>
              <span class="text-body-2">Gelb ab</span>
            </v-col>
            <v-col>
              <v-text-field v-model.number="laneColourYellow" type="number" variant="outlined" density="compact" hide-details />
            </v-col>
          </v-row>
          <v-row align="center" dense>
            <v-col cols="5" class="d-flex align-center ga-1">
              <v-icon color="error" size="16">mdi-circle</v-icon>
              <span class="text-body-2">Rot ab</span>
            </v-col>
            <v-col>
              <v-text-field v-model.number="laneColourRed" type="number" variant="outlined" density="compact" hide-details />
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions class="pb-4 px-4">
          <v-spacer />
          <v-btn color="primary" @click="showColorLimitsMobile = false">OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- ── Bahn-Zuweisung Dialog ── -->
    <CheckInAssignDialog
        v-model="showLaneDialog"
        :selectedSwimmer="selectedSwimmer"
        :recommendedLane="recommendedLane"
        :lanes="lanesWithCounts"
        :laneColourYellow="laneColourYellow"
        :laneColourRed="laneColourRed"
        :isSmallScreen="isSmallScreen"
        @assign="assignLane"
        @remove="removeFromLane"
    />

  </v-container>
</template>

<script>
import CheckInAssignDialog from "../components/swimmer-checkin/CheckInAssignDialog.vue";
import CheckInLanePanel from "../components/swimmer-checkin/CheckInLanePanel.vue";
import CheckInSearchBar from "../components/swimmer-checkin/CheckInSearchBar.vue";
import CheckInMobileLaneStrip from "../components/swimmer-checkin/CheckInMobileLaneStrip.vue";
import CheckInSwimmerList from "../components/swimmer-checkin/CheckInSwimmerList.vue";
import { useLaneStore, useSwimmerStore } from '../store/index.js';
import { participantService } from '../services/participantService.js';

export default {
  name: 'CheckIn',
  components: { CheckInSwimmerList, CheckInMobileLaneStrip, CheckInSearchBar, CheckInLanePanel, CheckInAssignDialog },
  data() {
    return {
      swimmers: [],             // Suchergebnisse vom Backend
      searchQuery: '',
      showLaneDialog: false,
      showColorLimitsMobile: false,
      selectedSwimmer: null,
      showKeyboard: false,
      recommendedLane: null,
      filterLane: [],
      laneColourYellow: 6,
      laneColourRed: 9,
      sortBy: 'id',
      sortAsc: true,
      statusFilter: [],
      sortOptions: [
        { key: 'id',                 label: 'Nummer' },
        { key: 'firstName',          label: 'Vorname' },
        { key: 'lastName',           label: 'Nachname' },
        { key: 'age',                label: 'Alter' },
        { key: 'averageTimeSeconds', label: 'Geschwindigkeit' },
        { key: 'distance',           label: 'Strecke' },
      ],
      alphabet: ['A','B','C','D','E','F','G','H','I','J','K','L','M',
                 'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                 'Ä','Ö','Ü'],
    };
  },
  setup() {
    const laneStore    = useLaneStore();
    const swimmerStore = useSwimmerStore();
    return { laneStore, swimmerStore };
  },
  async mounted() {
    await participantService.registerCheckin();
    this.swimmers = await participantService.searchAsYouType('');
  },
  watch: {
    async searchQuery(val) {
      this.swimmers = await participantService.searchAsYouType(val);
    },
  },
  computed: {
    // Bahnen aus dem Store + Auslastung aus den SignalR-Events (laneInfos)
    lanesWithCounts() {
      return this.laneStore.lanes.map(lane => {
        const info = this.swimmerStore.laneInfos[lane.id] ?? { participants: 0, averageTimeSeconds: null };
        return {
          ...lane,
          participants:       info.participants       ?? 0,
          activeParticipants: info.participants       ?? 0,   // Backend liefert keinen separaten Active-Count
          averageTimeSeconds: info.averageTimeSeconds ?? null,
        };
      });
    },

    // Lane- und Statusfilter + Sortierung — Textsuche macht das Backend
    filteredSwimmers() {
      let list = this.swimmers;

      if (this.filterLane.length > 0) {
        list = list.filter(s => this.filterLane.includes(s.lane));
      }

      if (this.statusFilter.length > 0) {
        list = list.filter(s =>
          (this.statusFilter.includes('unassigned') && !s.lane) ||
          (this.statusFilter.includes('assigned')   && s.lane && !s.isActive) ||
          (this.statusFilter.includes('active')     && s.isActive) ||
          (this.statusFilter.includes('better')     && this.betterLaneSuggestions.has(s.id))
        );
      }

      const key = this.sortBy;
      const dir = this.sortAsc ? 1 : -1;
      return [...list].sort((a, b) => {
        const aVal = key === 'distance' ? (a.lanes ?? 0) * 25 : (a[key] ?? null);
        const bVal = key === 'distance' ? (b.lanes ?? 0) * 25 : (b[key] ?? null);
        if (aVal == null && bVal == null) return 0;
        if (aVal == null) return 1 * dir;
        if (bVal == null) return -1 * dir;
        if (typeof aVal === 'string') return aVal.localeCompare(bVal) * dir;
        return (aVal - bVal) * dir;
      });
    },

    betterLaneSuggestions() {
      const ids = new Set();
      for (const swimmer of this.swimmers) {
        if (!swimmer.lane) continue;
        const rec = this.calcRecommendedLane(swimmer);
        if (rec && rec.id !== swimmer.lane) ids.add(swimmer.id);
      }
      return ids;
    },

    // Gesamtdistanz kommt als Bahnen-Summe vom Backend via updateTotalLanes
    totalDistanceFormatted() {
      const meters = this.swimmerStore.totalLanes * 25;
      return meters >= 1000
          ? (meters / 1000).toLocaleString('de-DE', { maximumFractionDigits: 2 }) + ' km'
          : meters.toLocaleString('de-DE') + ' m';
    },

    isSmallScreen() {
      return window.innerWidth < 600;
    },
  },
  methods: {
    clearFilter() {
      this.searchQuery = '';
    },

    openLaneDialog(swimmer) {
      this.selectedSwimmer = swimmer;
      this.recommendedLane = this.calcRecommendedLane(swimmer);
      this.showLaneDialog = true;
    },

    calcRecommendedLane(swimmer) {
      const availableLanes = this.lanesWithCounts.filter(l => l.participants < this.laneColourRed);
      if (!availableLanes.length) return null;

      const swimmerSpeed  = swimmer.averageTimeSeconds;
      const maxSpeedDiff  = Math.max(...availableLanes.map(l => Math.abs((l.averageTimeSeconds || swimmerSpeed) - swimmerSpeed))) || 1;
      const maxPart       = Math.max(...availableLanes.map(l => l.participants))       || 1;
      const maxActivePart = Math.max(...availableLanes.map(l => l.activeParticipants)) || 1;

      let bestLane = null;
      let bestScore = Infinity;
      for (const lane of availableLanes) {
        const laneTime       = lane.averageTimeSeconds || swimmerSpeed;
        const speedScore     = Math.abs(laneTime - swimmerSpeed) / maxSpeedDiff;
        const occupancyScore = lane.participants / maxPart;
        const activeScore    = lane.activeParticipants / maxActivePart;
        const score = speedScore * 0.55 + occupancyScore * 0.35 + activeScore * 0.10;
        if (score < bestScore) { bestScore = score; bestLane = lane; }
      }
      return bestLane;
    },

    async assignLane(laneId) {
      if (this.selectedSwimmer) {
        await participantService.assignToLane(laneId, this.selectedSwimmer.id);
        // Optimistisches Update der lokalen Suchergebnisse
        this.selectedSwimmer.lane     = laneId;
        this.selectedSwimmer.isActive = false;
      }
      this.showLaneDialog  = false;
      this.selectedSwimmer = null;
      this.recommendedLane = null;
    },

    async removeFromLane() {
      if (this.selectedSwimmer) {
        const laneId     = this.selectedSwimmer.lane;
        const swimmerId  = this.selectedSwimmer.id;
        // Optimistisches lokales Update
        this.selectedSwimmer.lane     = null;
        this.selectedSwimmer.isActive = false;
        this.showLaneDialog  = false;
        this.selectedSwimmer = null;
        this.recommendedLane = null;
        if (laneId != null) {
          try {
            await participantService.leaveLane(laneId, swimmerId);
          } catch (e) {
            console.warn('leaveLane fehlgeschlagen (ggf. vom Backend nicht unterstützt):', e.message);
          }
        }
      } else {
        this.showLaneDialog  = false;
        this.selectedSwimmer = null;
        this.recommendedLane = null;
      }
    },
  },
};
</script>

<style scoped>
/* ── Sticky Layout (alle Größen) ── */
.checkin-container {
  height: calc(100vh - 48px);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.checkin-row {
  flex: 1 1 0;
  min-height: 0;
  overflow: hidden;
}

.left-col {
  display: flex !important;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.search-area {
  flex: 0 0 auto;
}

.mobile-top {
  flex: 0 0 auto;
}

.swimmer-scroll {
  flex: 1 1 0;
  min-height: 0;
  overflow-y: auto;
  padding-right: 4px;
}

@media (min-width: 960px) {
  .right-col {
    height: 100%;
    overflow-y: auto;
  }
}
</style>
