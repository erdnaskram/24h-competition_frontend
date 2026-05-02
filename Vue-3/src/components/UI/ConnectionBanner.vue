<template>
  <!-- Warte auf erste Verbindung -->
  <v-snackbar
    v-model="showWaiting"
    color="info"
    location="bottom center"
    :timeout="-1"
    elevation="8"
    rounded="lg"
    z-index="9999"
  >
    <div class="d-flex align-center ga-2">
      <v-progress-circular indeterminate size="18" width="2" />
      <span>Warte auf Verbindung zum Backend…</span>
    </div>
  </v-snackbar>

  <!-- Verbindung verloren – bleibt bis sie wieder da ist -->
  <v-snackbar
    v-model="showError"
    color="error"
    location="bottom center"
    :timeout="-1"
    elevation="8"
    rounded="lg"
    z-index="9999"
  >
    <div class="d-flex align-center ga-2">
      <v-icon>mdi-wifi-off</v-icon>
      <span>Backend nicht erreichbar – Verbindung wird wiederhergestellt…</span>
    </div>
  </v-snackbar>

  <!-- Verbindung wiederhergestellt – verschwindet nach 3 s -->
  <v-snackbar
    v-model="showSuccess"
    color="success"
    location="bottom center"
    :timeout="3000"
    elevation="8"
    rounded="lg"
    z-index="9999"
  >
    <div class="d-flex align-center ga-2">
      <v-icon>mdi-wifi</v-icon>
      <span>Verbindung hergestellt</span>
    </div>
  </v-snackbar>
</template>

<script>
import { useSwimmerStore } from '../../store/SwimmerStore.js'

export default {
  name: 'ConnectionBanner',
  setup() {
    return { store: useSwimmerStore() }
  },
  data() {
    return {
      showWaiting:   false,
      showError:     false,
      showSuccess:   false,
      hadConnection: false,
    }
  },
  watch: {
    'store.isConnecting'(val) {
      if (val && !this.hadConnection) {
        // Erster Verbindungsaufbau läuft
        this.showWaiting = true
        this.showError   = false
      }
    },
    'store.isConnected'(isNowConnected) {
      if (isNowConnected) {
        const wasOffline   = this.showError || this.showWaiting
        this.showWaiting   = false
        this.showError     = false
        this.showSuccess   = wasOffline   // Erfolg nur zeigen wenn vorher offline
        this.hadConnection = true
      } else if (this.hadConnection) {
        // War verbunden, jetzt getrennt
        this.showSuccess = false
        this.showError   = true
      }
    },
    'store.connectionError'(err) {
      if (err && !this.hadConnection) {
        // Erste Verbindung schlug fehl
        this.showWaiting = false
        this.showError   = true
      }
    },
  },
}
</script>
