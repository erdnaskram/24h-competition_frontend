<template>
  <v-navigation-drawer
      v-model="drawer"
      location="right"
      temporary
      color="#013157"
  >
    <v-list-item
        prepend-icon="mdi-swim"
        title="DLRG 24 Hours"
        :subtitle="'Bahn ' + laneId"
        class="drawer-header-item brand-link"
    ></v-list-item>
    <v-list nav density="compact" class="mt-2">
      <v-list-item
          v-for="lane in laneStore.lanes"
          :key="'drawerLane' + lane.id"
          :title="lane.name"
          :to="'/lane/' + lane.id"
          :active="lane.id === laneId"
          active-color="white"
          class="drawer-item"
          :class="{ 'drawer-item-active': lane.id === laneId }"
          @click="drawer = false"
      ></v-list-item>
      <v-divider class="my-2" color="rgba(255,255,255,0.2)" />
      <v-list-item
          :title="'Schriftgröße: ' + textSizeLabel"
          prepend-icon="mdi-format-size"
          class="drawer-item"
          @click="cycleTextSize"
      ></v-list-item>
    </v-list>
  </v-navigation-drawer>

  <v-app-bar color="#013157" elevation="4" density="comfortable">
    <v-app-bar-title class="brand-link">
      DLRG 24 Hours – Bahn {{ laneId }}
    </v-app-bar-title>
    <!-- Desktop: Buttons direkt in der App-Bar -->
    <v-btn
        v-for="lane in laneStore.lanes"
        :key="'navLane' + lane.id"
        color="white"
        :variant="lane.id === laneId ? 'outlined' : 'text'"
        :class="lane.id === laneId ? 'active-lane-btn' : ''"
        :disabled="lane.id === laneId"
        :to="'/lane/' + lane.id"
        class="d-none d-lg-flex mx-1"
    >
      <v-icon v-if="lane.id === laneId" start>mdi-swim</v-icon>
      {{ lane.name }}
    </v-btn>
    <!-- Schriftgröße-Toggle -->
    <v-btn
        variant="text"
        color="white"
        title="Schriftgröße wechseln"
        @click="cycleTextSize"
    >
      <v-icon start>mdi-format-size</v-icon>{{ textSizeLabel }}
    </v-btn>
    <!-- Mobile/Tablet: Hamburger-Menü -->
    <v-app-bar-nav-icon
        color="white"
        class="d-lg-none"
        @click="drawer = !drawer"
    ></v-app-bar-nav-icon>
  </v-app-bar>

  <v-main style="padding-top: 0 !important;">
    <v-container fluid class="pa-2">
      <div class="lane-layout">
        <!-- Linke Spalte: Aktive + Ausgeblendet -->
        <div class="lane-left">
          <active-swimmer
              v-for="swimmer in activeSwimmers"
              :key="'activeSwimmer' + swimmer.id"
              :swimmer="swimmer"
              @editActiveSwimmer="editActiveSwimmer"
              @activeSwimmerClicked="rearrangeSwimmer"
          ></active-swimmer>

          <div v-if="minimizedSwimmers.length > 0"
               class="section-heading mt-2"
               @click="minimizedCollapsed = !minimizedCollapsed">
            <span>Ausgeblendet ({{ minimizedSwimmers.length }})</span>
            <v-icon size="20">{{ minimizedCollapsed ? 'mdi-chevron-down' : 'mdi-chevron-up' }}</v-icon>
          </div>
          <v-expand-transition>
            <div v-show="!minimizedCollapsed">
              <minimized-swimmer
                  v-for="swimmer in minimizedSwimmers"
                  :key="'minimizedSwimmer' + swimmer.id"
                  :swimmer="swimmer"
                  @showActiveSwimmer="showActiveSwimmer"
              ></minimized-swimmer>
            </div>
          </v-expand-transition>
        </div>

        <!-- Rechte Spalte: Wartend -->
        <div class="lane-right" :class="{ 'lane-right--collapsed': inactiveCollapsed }">
          <div v-if="inactiveSwimmers.length > 0"
               class="section-heading"
               @click="inactiveCollapsed = !inactiveCollapsed">
            <span>Wartend ({{ inactiveSwimmers.length }})</span>
            <v-icon size="20">{{ inactiveCollapsed ? 'mdi-chevron-down' : 'mdi-chevron-up' }}</v-icon>
          </div>
          <!-- Mobil: vertikales Einklappen -->
          <v-expand-transition>
            <div v-show="!inactiveCollapsed" class="d-lg-none">
              <inactive-swimmer
                  v-for="swimmer in inactiveSwimmers"
                  :key="'inactiveSwimmerMobile' + swimmer.id"
                  :swimmer="swimmer"
                  :hide-info-default="true"
                  @inactiveSwimmerClicked="inactiveSwimmerClicked"
              ></inactive-swimmer>
            </div>
          </v-expand-transition>
          <!-- PC: horizontales Einschieben -->
          <transition name="slide-right">
            <div v-show="!inactiveCollapsed" class="d-none d-lg-block">
              <inactive-swimmer
                  v-for="swimmer in inactiveSwimmers"
                  :key="'inactiveSwimmerDesktop' + swimmer.id"
                  :swimmer="swimmer"
                  :hide-info-default="true"
                  @inactiveSwimmerClicked="inactiveSwimmerClicked"
              ></inactive-swimmer>
            </div>
          </transition>
        </div>
      </div>
    </v-container>
  </v-main>

  <edit-inactive-swimmer-modal
      ref="displayInactiveSwimmerModal"
      @startSwimming="addSwimmerToActiveList"
      @leaveLane="removeSwimmerFromLane"
      @editSwimmer="editSwimmerCharacteristics"
      @close="">
  </edit-inactive-swimmer-modal>
  <edit-active-swimmer-modal
      ref="editActiveSwimmerModal"
      @breakSwimming="removeSwimmerFromActiveList"
      @leaveLane="removeSwimmerFromLane"
      @editSwimmer="editSwimmerCharacteristics"
      @minimizeSwimmer="minimizeSwimmer"
      @close="">
  </edit-active-swimmer-modal>
  <edit-swimmer-characteristics-modal
      ref="editSwimmerCharacteristicsModal"
      @saveChanges="saveSwimmerCharacteristicsChanges"
      @startSwimming="addSwimmerToActiveList"
      @close="">
  </edit-swimmer-characteristics-modal>
</template>

<script>
import {useLaneStore} from '../store';
import {useTextSize} from '../composables/useTextSize.js';
import InactiveSwimmer from "../components/swimmer-management/InactiveSwimmer.vue";
import EditInactiveSwimmerModal from "../components/swimmer-management/EditInactiveSwimmerModal.vue";
import ActiveSwimmer from "../components/swimmer-management/ActiveSwimmer.vue";
import EditActiveSwimmerModal from "../components/swimmer-management/EditActiveSwimmerModal.vue";
import EditSwimmerCharacteristicsModal from "../components/swimmer-management/EditSwimmerCharacteristicsModal.vue";
import MinimizedSwimmer from "../components/swimmer-management/MinimizedSwimmer.vue";

export default {
  name: "LaneMain",
  components: {
    MinimizedSwimmer,
    EditSwimmerCharacteristicsModal,
    EditInactiveSwimmerModal,
    EditActiveSwimmerModal,
    ActiveSwimmer,
    InactiveSwimmer,
  },
  data() {
    return {
      laneId: null,
      drawer: false,
      inactiveCollapsed: false,
      minimizedCollapsed: false,
      showEditSwimmerModal: false,
      showMessageModal: false,
      swimmers: [
        {
          id: 1,
          isActive: false,
          swimmerName: {
            first: "Max Alfred Klaus",
            last: "Mustermann"
          },
          age: 25,
          swimDistance: 1050,
          team: "Team A",
          family: "",
          gender: "male",
          characteristics: {
            swimwearType: "trunks",
            swimwearColor: "green",
            googles: "blue",
            swimCap: "red",
            hair: "brown",
            tattoo: false,
            headphones: false,
            notes: ""
          }
        },
        {
          id: 2,
          isActive: true,
          swimmerName: {first: "Erika", last: "Musterfrau"},
          age: 30,
          swimDistance: 1500,
          team: "",
          family: "Millers and Friends",
          gender: "female",
          characteristics: {
            swimwearType: "bikini",
            swimwearColor: "red",
            googles: "black",
            swimCap: "black",
            hair: "yellow",
            tattoo: false,
            headphones: true,
            notes: "Prefers to swim in the evening."
          }
        },
        {
          id: 3,
          isActive: false,
          swimmerName: {
            first: "Hans",
            last: "Müller"
          },
          age: 28,
          swimDistance: 20350,
          team: "Team B",
          family: "",
          gender: "male",
          characteristics: {
            swimwearType: "trunks",
            swimwearColor: "red",
            googles: "black",
            swimCap: "none",
            hair: "black",
            tattoo: true,
            headphones: true,
            notes: "Has a tattoo on his left arm."
          }
        },
        {
          id: 4,
          isActive: false,
          swimmerName: {
            first: "Alex",
            last: "Schmidt"
          },
          age: 22,
          swimDistance: 5000,
          team: "Team A",
          family: "",
          gender: "diverse",
          characteristics: {
            swimwearType: "swimsuit",
            swimwearColor: "blue",
            googles: "none",
            swimCap: "yellow",
            hair: "green",
            tattoo: false,
            headphones: false,
            notes: "Prefers to swim in the morning."
          }
        },
        {
          id: 5,
          isActive: false,
          swimmerName: {
            first: "Peter",
            last: "Schneider"
          },
          age: 35,
          swimDistance: 10000,
          team: "",
          family: "",
          gender: "male",
          characteristics: {
            swimwearType: "short-pants",
            swimwearColor: "blue",
            googles: "green",
            swimCap: "white",
            hair: "grey",
            tattoo: false,
            headphones: true,
            notes: "Enjoys listening to music while swimming."
          }
        },
        {
          id: 6,
          isActive: false,
          swimmerName: {
            first: "Julia",
            last: "Fischer"
          },
          age: 8,
          swimDistance: 2000,
          team: "",
          family: "Millers",
          gender: "female",
          characteristics: {
            swimwearType: "bikini",
            swimwearColor: "pink",
            googles: "purple",
            swimCap: "pink",
            hair: "yellow",
            tattoo: false,
            headphones: false,
            notes: "Loves swimming with her friends."
          }
        },
      ],
      selectedInactiveSwimmer: null,
    };
  },
  setup() {
    const laneStore = useLaneStore();
    const { textSizeLabel, cycleTextSize } = useTextSize();
    return {laneStore, textSizeLabel, cycleTextSize};
  },
  mounted() {
    this.laneId = Number.parseInt(this.$route.params.id);
  },
  watch: {
    '$route.params.id'(newId) {
      this.laneId = Number.parseInt(newId);
    }
  },
  computed: {
    activeSwimmers() {
      return this.swimmers.filter(s => s.isActive && !s.isMinimized)
    },
    inactiveSwimmers() {
      return this.swimmers.filter(s => !s.isActive)
    },
    minimizedSwimmers() {
      return this.swimmers.filter(s => s.isActive && s.isMinimized)
    }
  },
  methods: {
    inactiveSwimmerClicked(swimmer) {
      this.selectedInactiveSwimmer = swimmer;
      this.$refs.displayInactiveSwimmerModal.openModal(swimmer);
    },
    addSwimmerToActiveList(swimmer) {
      swimmer.isActive = true;
      const index = this.swimmers.findIndex(s => s.id === swimmer.id);
      if (index !== -1) {
        this.swimmers.splice(index, 1);
        this.swimmers.push(swimmer);
      }
    },
    removeSwimmerFromLane(swimmer) {
      this.swimmers = this.swimmers.filter(s => s.id !== swimmer.id);
    },
    editActiveSwimmer(swimmer) {
      this.$refs.editActiveSwimmerModal.openModal(swimmer);
    },
    removeSwimmerFromActiveList(swimmer) {
      swimmer.isActive = false;
    },
    editSwimmerCharacteristics(swimmer, startSwimming = false) {
      this.$refs.editSwimmerCharacteristicsModal.openModal(swimmer, startSwimming);
    },
    saveSwimmerCharacteristicsChanges(updatedSwimmer) {
      const index = this.swimmers.findIndex(s => s.id === updatedSwimmer.id);
      if (index !== -1) {
        this.swimmers.splice(index, 1, updatedSwimmer);
      }
    },
    minimizeSwimmer(swimmer) {
      swimmer.isMinimized = true;
    },
    showActiveSwimmer(swimmer) {
      swimmer.isMinimized = false;
      this.rearrangeSwimmer(swimmer);
    },
    rearrangeSwimmer(swimmer) {
      if (this.activeSwimmers.length > 1) {
        const index = this.swimmers.findIndex(s => s.id === swimmer.id);
        if (index !== -1) {
          this.swimmers.splice(index, 1);
          this.swimmers.push(swimmer);
        }
      }
    }
  }
}
</script>

<style scoped>
.lane-layout {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.lane-left {
  width: 100%;
  min-width: 0;
}

.lane-right {
  width: 100%;
  padding-bottom: 16px;
}

@media (min-width: 1025px) {
  .lane-layout {
    flex-direction: row;
    align-items: flex-start;
  }
  .lane-left {
    flex: 1;
    min-width: 0;
  }
  .lane-right {
    width: 33.333%;
    flex-shrink: 0;
    overflow: hidden;
    transition: width 0.3s ease;
  }
  .lane-right--collapsed {
    width: 44px;
  }
  .lane-right--collapsed .section-heading {
    writing-mode: vertical-rl;
    transform: rotate(180deg);
    padding: 12px 6px;
    justify-content: center;
    height: auto;
    width: 44px;
  }
  .lane-right--collapsed .section-heading :deep(.v-icon) {
    display: none;
  }
}

.section-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 1.1rem;
  font-weight: bold;
  color: #013157;
  padding: 6px 8px;
  cursor: pointer;
  user-select: none;
  border-radius: 6px;
}

.section-heading:hover {
  background-color: rgba(1, 49, 87, 0.06);
}

.slide-right-enter-active,
.slide-right-leave-active {
  transition: transform 0.3s ease, opacity 0.3s ease;
  overflow: hidden;
}

.slide-right-enter-from,
.slide-right-leave-to {
  transform: translateX(100%);
  opacity: 0;
}

.active-lane-btn {
  font-weight: bold;
  opacity: 1 !important;
}

.brand-link {
  color: white;
  text-decoration: none;
  font-size: 1.3rem;
  font-weight: bold;
  letter-spacing: 0.02em;
}

.drawer-header-item {
  color: white !important;
}

.drawer-item {
  color: rgba(255, 255, 255, 0.8) !important;
}

.drawer-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
  color: white !important;
}

.drawer-item-active {
  color: white !important;
  font-weight: bold;
}
</style>