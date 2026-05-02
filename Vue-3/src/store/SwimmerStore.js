// store/SwimmerStore.js
import { defineStore } from 'pinia'

export const useSwimmerStore = defineStore('swimmer', {
    state: () => ({
        /** Alle Schwimmer der aktuell geöffneten Bahn */
        swimmers: [],
        /** Verbindungsstatus zum Backend */
        isConnected: false,
        isConnecting: false,
        connectionError: null,
        /** Gesamt-Bahnen über alle Bahnen (für Check-In-Anzeige) */
        totalLanes: 0,
        /** Lane-Info pro Bahn: { [laneNo]: { participants, averageTimeSeconds } } */
        laneInfos: {},
    }),

    getters: {
        activeSwimmers: (state) => state.swimmers
            .filter(s => s.isActive && !s.isMinimized)
            .sort((a, b) => {
                const etaA = (a.lastCountTime ?? 0) + (a.averageTimeSeconds ?? 120) * 1000
                const etaB = (b.lastCountTime ?? 0) + (b.averageTimeSeconds ?? 120) * 1000
                return etaA - etaB
            }),
        minimizedSwimmers: (state) => state.swimmers
            .filter(s => s.isActive && s.isMinimized)
            .sort((a, b) => {
                const etaA = (a.lastCountTime ?? 0) + (a.averageTimeSeconds ?? 120) * 1000
                const etaB = (b.lastCountTime ?? 0) + (b.averageTimeSeconds ?? 120) * 1000
                return etaA - etaB
            }),
        inactiveSwimmers:  (state) => state.swimmers.filter(s => !s.isActive),
    },

    actions: {
        // ── Vom Service/Adapter befüllt ──────────────────────────────────────

        /** Setzt die komplette Schwimmerliste (beim register-Event) */
        setSwimmers(list) {
            this.swimmers = list
        },

        /**
         * Fügt einen Schwimmer hinzu oder aktualisiert ihn (addParticipant / updateParticipant).
         * isMinimized wird beim Update beibehalten, sofern der Schwimmer schon bekannt ist.
         */
        upsertSwimmer(swimmer) {
            const idx = this.swimmers.findIndex(s => s.id === swimmer.id)
            if (idx === -1) {
                this.swimmers.push(swimmer)
            } else {
                // isMinimized ist reiner Frontend-State – nicht vom Backend überschreiben
                swimmer.isMinimized = this.swimmers[idx].isMinimized
                this.swimmers.splice(idx, 1, swimmer)
            }
        },

        /** Entfernt einen Schwimmer (removeParticipant) */
        removeSwimmer(id) {
            this.swimmers = this.swimmers.filter(s => s.id !== id)
        },

        /** Aktualisiert die Bahn-Statistik (updateLaneInfo vom Check-In) */
        updateLaneInfo(laneNo, info) {
            this.laneInfos = { ...this.laneInfos, [laneNo]: info }
        },

        /** Aktualisiert die Gesamt-Bahnen (updateTotalLanes) */
        setTotalLanes(n) {
            this.totalLanes = n
        },

        setConnecting() {
            this.isConnecting = true
        },

        setConnected(val) {
            this.isConnecting = false
            this.isConnected  = val
            if (val) this.connectionError = null
        },

        setConnectionError(err) {
            this.isConnecting    = false
            this.isConnected     = false
            this.connectionError = err
        },

        // ── Reiner Frontend-State (kein Backend-Call) ─────────────────────────

        /** Blendet einen aktiven Schwimmer auf die Mini-Zeile ein/aus */
        setMinimized(id, val) {
            const s = this.swimmers.find(s => s.id === id)
            if (s) s.isMinimized = val
        },

        /** Leert den Store beim Verlassen einer Bahn (Verbindungsstatus bleibt erhalten) */
        reset() {
            this.swimmers = []
        },
    },
})
