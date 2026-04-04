<template>
  <div>
    <v-text-field
        :model-value="search"
        label="Schwimmer suchen"
        prepend-inner-icon="mdi-magnify"
        clearable
        persistent-clear
        variant="outlined"
        density="comfortable"
        autofocus
        hide-details
        @update:model-value="$emit('update:search', $event); $emit('search-input')"
        @click:clear="$emit('clear')"
    />

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

    <!-- Aktiver Bahn-Filter -->
    <div v-if="filterLane" class="d-flex align-center mt-3 mb-3 ga-2">
      <v-chip
          color="#e8f0f7"
          size="large"
          prepend-icon="mdi-filter"
          closable
          @click:close="$emit('update:filterLane', null)"
          style="color: #013157; font-weight: bold; font-size: 1rem; border: 2px solid #013157; background-color: #e8f0f7 !important;"
      >
        Bahn {{ filterLane }}
      </v-chip>
      <span class="text-body-2 text-medium-emphasis">– Bahn zum Zurücksetzen</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CheckInSearchBar',
  props: {
    search:       { type: String,  default: '' },
    showKeyboard: { type: Boolean, default: false },
    filterLane:   { type: Number,  default: null },
    alphabet:     { type: Array,   required: true },
  },
  emits: ['update:search', 'update:showKeyboard', 'update:filterLane', 'search-input', 'clear'],
  data() {
    return {
      isTouchDevice: window.matchMedia('(pointer: coarse)').matches,
    };
  },
  methods: {
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
