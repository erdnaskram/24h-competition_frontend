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

export default {
  name: 'CheckIn',
  components: {CheckInSwimmerList, CheckInMobileLaneStrip, CheckInSearchBar, CheckInLanePanel, CheckInAssignDialog},
  data() {
    return {
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

      lanes: [
        { id: 1, averageTimeSeconds: 108 },
        { id: 2, averageTimeSeconds: 92  },
        { id: 3, averageTimeSeconds: 85  },
        { id: 4, averageTimeSeconds: 101 },
        { id: 5, averageTimeSeconds: 97  },
      ],

      swimmers: [
        { id:  1, firstName: 'Anna',      lastName: 'Müller',      age: 28, gender: 'f', lanes: 14, averageTimeSeconds:  95, lane: 2,    isActive: true  },
        { id:  2, firstName: 'Max',       lastName: 'Mustermann',  age: 35, gender: 'm', lanes: 28, averageTimeSeconds: 110, lane: null, isActive: false },
        { id:  3, firstName: 'Lisa',      lastName: 'Schmidt',     age: 22, gender: 'f', lanes:  6, averageTimeSeconds:  88, lane: 3,    isActive: false },
        { id:  4, firstName: 'Thomas',    lastName: 'Bauer',       age: 40, gender: 'm', lanes: 42, averageTimeSeconds: 102, lane: 4,    isActive: true  },
        { id:  5, firstName: 'Sandra',    lastName: 'Hoffmann',    age: 31, gender: 'f', lanes: 19, averageTimeSeconds:  91, lane: null, isActive: false },
        { id:  6, firstName: 'Klaus',     lastName: 'Fischer',     age: 47, gender: 'm', lanes: 55, averageTimeSeconds: 118, lane: 1,    isActive: true  },
        { id:  7, firstName: 'Julia',     lastName: 'Weber',       age: 25, gender: 'f', lanes:  8, averageTimeSeconds:  84, lane: 3,    isActive: false },
        { id:  8, firstName: 'Peter',     lastName: 'Wagner',      age: 52, gender: 'm', lanes: 67, averageTimeSeconds: 125, lane: 1,    isActive: true  },
        { id:  9, firstName: 'Sarah',     lastName: 'Schulz',      age: 19, gender: 'f', lanes:  3, averageTimeSeconds:  79, lane: null, isActive: false },
        { id: 10, firstName: 'Lena',      lastName: 'Maier',       age: 20, gender: 'f', lanes:  0, averageTimeSeconds: null, lane: null, isActive: false },
        { id: 11, firstName: 'Michael',   lastName: 'Zimmermann',  age: 38, gender: 'm', lanes: 33, averageTimeSeconds: 105, lane: 4,    isActive: false },
        { id: 12, firstName: 'Franziska', lastName: 'Becker',      age: 27, gender: 'f', lanes: 11, averageTimeSeconds:  93, lane: 2,    isActive: false },
        { id: 13, firstName: 'Stefan',    lastName: 'Krause',      age: 44, gender: 'm', lanes: 48, averageTimeSeconds: 113, lane: 1,    isActive: true  },
        { id: 14, firstName: 'Nicole',    lastName: 'Schäfer',     age: 33, gender: 'f', lanes: 22, averageTimeSeconds:  89, lane: null, isActive: false },
        { id: 15, firstName: 'Andreas',   lastName: 'Richter',     age: 29, gender: 'm', lanes: 17, averageTimeSeconds:  99, lane: 5,    isActive: false },
        { id: 16, firstName: 'Petra',     lastName: 'Weiß',        age: 55, gender: 'f', lanes: 71, averageTimeSeconds: 130, lane: 1,    isActive: true  },
        { id: 17, firstName: 'Martin',    lastName: 'Koch',        age: 32, gender: 'm', lanes: 35, averageTimeSeconds: 107, lane: 4,    isActive: true  },
        { id: 18, firstName: 'Sabine',    lastName: 'Klein',       age: 26, gender: 'f', lanes: 12, averageTimeSeconds:  86, lane: 3,    isActive: false },
        { id: 19, firstName: 'Jürgen',    lastName: 'Wolf',        age: 48, gender: 'm', lanes: 59, averageTimeSeconds: 121, lane: null, isActive: false },
        { id: 20, firstName: 'Jonas',     lastName: 'Huber',       age: 17, gender: 'm', lanes:  0, averageTimeSeconds: null, lane: null, isActive: false },
        { id: 21, firstName: 'Monika',    lastName: 'Braun',       age: 41, gender: 'f', lanes: 26, averageTimeSeconds:  97, lane: 5,    isActive: true  },
        { id: 22, firstName: 'Tobias',    lastName: 'Lange',       age: 23, gender: 'm', lanes:  9, averageTimeSeconds:  82, lane: 3,    isActive: false },
        { id: 23, firstName: 'Karin',     lastName: 'Schulze',     age: 36, gender: 'f', lanes: 31, averageTimeSeconds:  94, lane: 2,    isActive: true  },
        { id: 24, firstName: 'Ralf',      lastName: 'Meier',       age: 51, gender: 'm', lanes: 61, averageTimeSeconds: 116, lane: 1,    isActive: false },
        { id: 25, firstName: 'Anja',      lastName: 'Krüger',      age: 30, gender: 'f', lanes: 18, averageTimeSeconds:  90, lane: null, isActive: false },
        { id: 26, firstName: 'Frank',     lastName: 'Lehmann',     age: 45, gender: 'm', lanes: 44, averageTimeSeconds: 111, lane: 4,    isActive: true  },
        { id: 27, firstName: 'Heike',     lastName: 'Schmitt',     age: 39, gender: 'f', lanes: 37, averageTimeSeconds:  98, lane: 5,    isActive: true  },
        { id: 28, firstName: 'Uwe',       lastName: 'Günther',     age: 57, gender: 'm', lanes: 73, averageTimeSeconds: 128, lane: null, isActive: false },
        { id: 29, firstName: 'Claudia',   lastName: 'Hartmann',    age: 34, gender: 'f', lanes: 24, averageTimeSeconds:  92, lane: 2,    isActive: false },
        { id: 30, firstName: 'Bernd',     lastName: 'Sommer',      age: 42, gender: 'm', lanes: 46, averageTimeSeconds: 103, lane: 4,    isActive: false },
        { id: 31, firstName: 'Birgit',    lastName: 'Franke',      age: 29, gender: 'f', lanes: 16, averageTimeSeconds:  87, lane: 3,    isActive: true  },
        { id: 32, firstName: 'Emma',      lastName: 'Neumann',     age: 25, gender: 'f', lanes:  0, averageTimeSeconds: null, lane: 3,    isActive: false },
        { id: 33, firstName: 'Holger',    lastName: 'Pohl',        age: 37, gender: 'm', lanes: 39, averageTimeSeconds: 100, lane: 5,    isActive: false },
        { id: 34, firstName: 'Silvia',    lastName: 'Vogt',        age: 24, gender: 'f', lanes:  7, averageTimeSeconds:  83, lane: null, isActive: false },
        { id: 35, firstName: 'Dirk',      lastName: 'Roth',        age: 50, gender: 'm', lanes: 64, averageTimeSeconds: 119, lane: 1,    isActive: true  },
        { id: 36, firstName: 'Susanne',   lastName: 'Beck',        age: 32, gender: 'f', lanes: 20, averageTimeSeconds:  96, lane: 5,    isActive: false },
        { id: 37, firstName: 'Günter',    lastName: 'Werner',      age: 60, gender: 'm', lanes: 78, averageTimeSeconds: 132, lane: null, isActive: false },
        { id: 38, firstName: 'Melanie',   lastName: 'Böhm',        age: 27, gender: 'f', lanes: 13, averageTimeSeconds:  88, lane: 2,    isActive: true  },
        { id: 39, firstName: 'Patrick',   lastName: 'Brandt',      age: 21, gender: 'm', lanes:  5, averageTimeSeconds:  78, lane: null, isActive: false },
        { id: 40, firstName: 'Renate',    lastName: 'Seifert',     age: 46, gender: 'f', lanes: 52, averageTimeSeconds: 112, lane: null, isActive: false },
        { id: 41, firstName: 'Felix',     lastName: 'Kaufmann',    age: 31, gender: 'm', lanes:  0, averageTimeSeconds: null, lane: null, isActive: false },
        { id: 42, firstName: 'Norbert',   lastName: 'Engel',       age: 53, gender: 'm', lanes: 68, averageTimeSeconds: 123, lane: 1,    isActive: false },
        { id: 43, firstName: 'Christine', lastName: 'Pfeiffer',    age: 38, gender: 'f', lanes: 29, averageTimeSeconds:  93, lane: 2,    isActive: true  },
        { id: 44, firstName: 'Stephan',   lastName: 'Ziegler',     age: 43, gender: 'm', lanes: 41, averageTimeSeconds: 104, lane: 4,    isActive: false },
      ],
    };
  },
  computed: {
    lanesWithCounts() {
      return this.lanes.map(lane => {
        const onLane = this.swimmers.filter(s => s.lane === lane.id);
        const participants = onLane.length;
        const activeParticipants = onLane.filter(s => s.isActive).length;
        const averageTimeSeconds = participants > 0
            ? Math.round(onLane.reduce((sum, s) => sum + s.averageTimeSeconds, 0) / participants)
            : null;
        return { ...lane, participants, activeParticipants, averageTimeSeconds };
      });
    },

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

      if (this.searchQuery && this.searchQuery.trim()) {
        const q = this.searchQuery.trim().toLowerCase();
        list = list.filter(s =>
            s.firstName.toLowerCase().includes(q) ||
            s.lastName.toLowerCase().includes(q) ||
            String(s.id).includes(q)
        );
      }

      const key = this.sortBy;
      const dir = this.sortAsc ? 1 : -1;
      return [...list].sort((a, b) => {
        const aVal = key === 'distance' ? a.lanes * 25 : a[key];
        const bVal = key === 'distance' ? b.lanes * 25 : b[key];
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

    totalDistanceFormatted() {
      const totalLanes = this.swimmers.reduce((sum, s) => sum + s.lanes, 0);
      const meters = totalLanes * 25;
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

      const swimmerSpeed = swimmer.averageTimeSeconds;
      const maxSpeedDiff = Math.max(
          ...availableLanes.map(l => Math.abs((l.averageTimeSeconds || swimmerSpeed) - swimmerSpeed))
      ) || 1;
      const maxParticipants      = Math.max(...availableLanes.map(l => l.participants))       || 1;
      const maxActiveParticipants = Math.max(...availableLanes.map(l => l.activeParticipants)) || 1;

      let bestLane = null;
      let bestScore = Infinity;

      for (const lane of availableLanes) {
        const laneTime       = lane.averageTimeSeconds || swimmerSpeed;
        const speedScore     = Math.abs(laneTime - swimmerSpeed) / maxSpeedDiff;
        const occupancyScore = lane.participants / maxParticipants;
        const activeScore    = lane.activeParticipants / maxActiveParticipants;
        const score = speedScore * 0.55 + occupancyScore * 0.35 + activeScore * 0.10;
        if (score < bestScore) {
          bestScore = score;
          bestLane = lane;
        }
      }
      return bestLane;
    },

    assignLane(laneId) {
      if (this.selectedSwimmer) {
        this.selectedSwimmer.lane = laneId;
        this.selectedSwimmer.isActive = false;
      }
      this.showLaneDialog = false;
      this.selectedSwimmer = null;
      this.recommendedLane = null;
    },

    removeFromLane() {
      if (this.selectedSwimmer) {
        this.selectedSwimmer.lane = null;
        this.selectedSwimmer.isActive = false;
      }
      this.showLaneDialog = false;
      this.selectedSwimmer = null;
      this.recommendedLane = null;
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
