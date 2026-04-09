<template>
  <v-app-bar color="#013157" elevation="4" density="comfortable">
    <v-app-bar-title>
      <router-link to="/" class="brand-link">DLRG 24 Hours</router-link>
    </v-app-bar-title>

    <!-- Desktop-Navigation -->
    <template v-slot:append>
      <div class="d-none d-md-flex align-center ga-1">
        <v-menu>
          <template v-slot:activator="{ props }">
            <v-btn v-bind="props" variant="text" color="white" append-icon="mdi-chevron-down">
              Bahnen
            </v-btn>
          </template>
          <v-list density="compact" bg-color="#013157">
            <v-list-item
              title="Bahn Auswahl"
              to="/lane"
              class="nav-dropdown-item"
            />
            <v-list-item
              v-for="lane in laneStore.laneCount"
              :key="'dropDownItemLane' + lane"
              :title="'Bahn ' + lane"
              :to="'/lane/' + lane"
              class="nav-dropdown-item"
            />
          </v-list>
        </v-menu>

        <v-btn variant="text" color="white" to="/results">Ergebnisse</v-btn>
        <v-btn variant="text" color="white" to="/checkin">Check-In</v-btn>

        <v-btn
          variant="text"
          color="white"
          class="ml-2"
          title="Schriftgröße wechseln"
          @click="cycleTextSize"
        >
          <v-icon start>mdi-format-size</v-icon>{{ textSizeLabel }}
        </v-btn>
        <v-btn
          icon
          variant="text"
          color="white"
          :title="isDark ? 'Hell-Modus' : 'Dunkel-Modus'"
          @click="toggleDarkMode"
        >
          <v-icon>{{ isDark ? 'mdi-weather-sunny' : 'mdi-weather-night' }}</v-icon>
        </v-btn>
      </div>

      <!-- Hamburger für Mobile/Tablet -->
      <v-app-bar-nav-icon
        class="d-flex d-md-none"
        color="white"
        @click="drawer = !drawer"
      />
    </template>
  </v-app-bar>

  <!-- Mobile Navigation Drawer -->
  <v-navigation-drawer v-model="drawer" location="right" temporary color="#013157">
    <v-list nav density="compact" class="mt-2">
      <v-list-group value="Bahnen">
        <template v-slot:activator="{ props }">
          <v-list-item v-bind="props" title="Bahnen" class="drawer-item" />
        </template>
        <v-list-item
          title="Bahn Auswahl"
          to="/lane"
          class="drawer-sub-item"
          @click="drawer = false"
        />
        <v-list-item
          v-for="lane in laneStore.laneCount"
          :key="'drawerLane' + lane"
          :title="'Bahn ' + lane"
          :to="'/lane/' + lane"
          class="drawer-sub-item"
          @click="drawer = false"
        />
      </v-list-group>

      <v-list-item
        title="Ergebnisse"
        to="/results"
        class="drawer-item"
        @click="drawer = false"
      />
      <v-list-item
        title="Check-In"
        to="/checkin"
        class="drawer-item"
        @click="drawer = false"
      />
      <v-divider class="my-2" color="rgba(255,255,255,0.2)" />
      <v-list-item
        :title="'Schriftgröße: ' + textSizeLabel"
        prepend-icon="mdi-format-size"
        class="drawer-item"
        @click="cycleTextSize"
      />
      <v-list-item
        :title="isDark ? 'Hell-Modus' : 'Dunkel-Modus'"
        :prepend-icon="isDark ? 'mdi-weather-sunny' : 'mdi-weather-night'"
        class="drawer-item"
        @click="toggleDarkMode"
      />
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import { useLaneStore } from "../../store/index.js";
import { useTextSize } from "../../composables/useTextSize.js";
import { useDarkMode } from "../../composables/useDarkMode.js";

export default {
  name: "TheHeader",
  setup() {
    const laneStore = useLaneStore();
    const { textSizeLabel, cycleTextSize } = useTextSize();
    const { isDark, toggleDarkMode } = useDarkMode();
    return { laneStore, textSizeLabel, cycleTextSize, isDark, toggleDarkMode };
  },
  data() {
    return {
      drawer: false,
    };
  },
};
</script>

<style scoped>
.brand-link {
  color: white;
  text-decoration: none;
  font-size: 1.3rem;
  font-weight: bold;
  letter-spacing: 0.02em;
}

.nav-dropdown-item {
  color: white !important;
}

.nav-dropdown-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

.drawer-item {
  color: white !important;
  font-size: 1rem;
}

.drawer-sub-item {
  color: rgba(255, 255, 255, 0.8) !important;
}

.drawer-item:hover,
.drawer-sub-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}
</style>