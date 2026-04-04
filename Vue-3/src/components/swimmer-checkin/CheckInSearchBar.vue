<template>
  <div>
    <!-- Suchfeld + Filterknopf -->
    <div class="d-flex align-center ga-2">
      <v-text-field
          :model-value="search"
          label="Schwimmer suchen"
          prepend-inner-icon="mdi-magnify"
          clearable
          persistent-clear
          variant="outlined"
          density="comfortable"
          :autofocus="!isTouchDevice"
          hide-details
          style="flex: 1;"
          @update:model-value="$emit('update:search', $event); $emit('search-input')"
          @click:clear="$emit('clear')"
      />
      <v-btn
          icon
          variant="tonal"
          :style="showFilters || isActiveFilter || isNonDefaultSort
            ? 'background-color: #013157 !important; color: white !important;'
            : ''"
          @click="showFilters = !showFilters"
      >
        <v-badge
            v-if="activeCount > 0"
            :content="activeCount"
            color="error"
            floating
        >
          <v-icon>mdi-filter</v-icon>
        </v-badge>
        <v-icon v-else>mdi-filter</v-icon>
      </v-btn>
    </div>

    <!-- Tastatur-Toggle (nur auf Maus-Geräten) -->
    <div v-if="!isTouchDevice" class="mt-3 mb-2">
      <v-btn
          :prepend-icon="showKeyboard ? 'mdi-keyboard-off' : 'mdi-keyboard'"
          variant="tonal"
          size="small"
          @click="$emit('update:showKeyboard', !showKeyboard)"
      >
        {{ showKeyboard ? 'Tastatur ausblenden' : 'Tastatur einblenden' }}
      </v-btn>
    </div>

    <!-- Bildschirm-Tastatur -->
    <div v-if="showKeyboard" class="keyboard mt-2 mb-3">
      <v-btn v-for="c in 'ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ'.split('')" :key="c" variant="tonal" class="key-btn" @click="typeChar(c)">{{ c }}</v-btn>
      <v-btn v-for="n in '1234567890'.split('')" :key="'n'+n" variant="tonal" class="key-btn key-number" @click="typeChar(n)">{{ n }}</v-btn>
      <v-btn variant="tonal" color="error" class="key-btn key-backspace" @click="backspace">
        <v-icon size="16">mdi-backspace-outline</v-icon>
      </v-btn>
    </div>

    <!-- Filter & Sortierung -->
    <div v-if="showFilters">
      <!-- Statusfilter + Bahnfilter -->
      <div class="text-caption text-medium-emphasis mt-2 mb-1">Filter</div>
      <div class="sort-chips">
        <v-chip
            v-for="lane in lanes"
            :key="'lane-' + lane.id"
            size="small"
            :style="filterLane.includes(lane.id)
              ? 'background-color: #013157 !important; color: white !important;'
              : 'background-color: #f0f0f0 !important; color: #555 !important;'"
            @click="$emit('update:filterLane', filterLane.includes(lane.id) ? filterLane.filter(id => id !== lane.id) : [...filterLane, lane.id])"
        >Bahn {{ lane.id }}</v-chip>
        <v-chip
            v-for="opt in statusOptions"
            :key="opt.key"
            size="small"
            :style="statusFilter.includes(opt.key)
              ? 'background-color: #013157 !important; color: white !important;'
              : 'background-color: #f0f0f0 !important; color: #555 !important;'"
            :prepend-icon="opt.icon"
            @click="onStatusClick(opt.key)"
        >{{ opt.label }}</v-chip>
      </div>

      <!-- Sortierung -->
      <div class="text-caption text-medium-emphasis mt-3 mb-1">Sortierung</div>
      <div class="sort-chips">
        <v-chip
            v-for="opt in sortOptions"
            :key="opt.key"
            size="small"
            :style="sortBy === opt.key
              ? 'background-color: #013157 !important; color: white !important;'
              : 'background-color: #f0f0f0 !important; color: #555 !important;'"
            @click="onSortClick(opt.key)"
        >
          {{ opt.label }}
          <v-icon v-if="sortBy === opt.key" end size="12">
            {{ sortAsc ? 'mdi-arrow-up' : 'mdi-arrow-down' }}
          </v-icon>
        </v-chip>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CheckInSearchBar',
  props: {
    search:       { type: String,  default: '' },
    showKeyboard: { type: Boolean, default: false },
    filterLane:   { type: Array,   default: () => [] },
    alphabet:     { type: Array,   required: true },
    lanes:        { type: Array,   default: () => [] },
    sortOptions:  { type: Array,   default: () => [] },
    sortBy:       { type: String,  default: 'id' },
    sortAsc:      { type: Boolean, default: true },
    statusFilter: { type: Array,   default: () => [] },
  },
  emits: ['update:search', 'update:showKeyboard', 'update:filterLane', 'update:sortBy', 'update:sortAsc', 'update:statusFilter', 'search-input', 'clear'],
  computed: {
    isNonDefaultSort() {
      return this.sortBy !== 'id' || !this.sortAsc;
    },
    isActiveFilter() {
      return this.statusFilter.length > 0 || this.filterLane.length > 0;
    },
    activeCount() {
      let n = this.statusFilter.length + this.filterLane.length;
      if (this.isNonDefaultSort) n++;
      return n;
    },
    statusOptions() {
      return [
        { key: 'unassigned', label: 'Nicht eingeteilt', icon: 'mdi-account-off-outline' },
        { key: 'assigned',   label: 'Eingeteilt',       icon: 'mdi-account-check-outline' },
        { key: 'active',     label: 'Aktiv',            icon: 'mdi-swim' },
        { key: 'better',     label: 'Wechselvorschlag', icon: 'mdi-swap-horizontal' },
      ];
    },
  },
  data() {
    return {
      isTouchDevice: window.matchMedia('(pointer: coarse)').matches,
      showFilters: false,
    };
  },
  methods: {
    onStatusClick(key) {
      const next = this.statusFilter.includes(key)
        ? this.statusFilter.filter(k => k !== key)
        : [...this.statusFilter, key];
      this.$emit('update:statusFilter', next);
    },
    onSortClick(key) {
      if (this.sortBy === key) {
        this.$emit('update:sortAsc', !this.sortAsc);
      } else {
        this.$emit('update:sortBy', key);
        this.$emit('update:sortAsc', true);
      }
    },
    typeChar(char) {
      this.$emit('update:search', (this.search || '') + char);
      this.$emit('search-input');
    },
    backspace() {
      this.$emit('update:search', (this.search || '').slice(0, -1));
      this.$emit('search-input');
    },
  },
};
</script>

<style scoped>
:deep(.v-text-field) { margin-bottom: 0 !important; }

.sort-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.keyboard {
  display: flex;
  flex-wrap: wrap;
  gap: 3px;
}

.key-btn {
  min-width: 46px !important;
  min-height: 46px !important;
  height: 46px !important;
  font-weight: bold;
  font-size: 1rem !important;
  padding: 0 6px !important;
}

.key-number {
  background-color: rgba(1, 49, 87, 0.1) !important;
}

.key-backspace {
  min-width: 52px !important;
  max-width: 52px !important;
}
</style>
