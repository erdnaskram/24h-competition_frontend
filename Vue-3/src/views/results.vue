<template>
  <!-- Navbar erscheint wenn Maus in die oberen 60px geht -->
  <the-header v-if="navOpen" />

  <section class="tv-screen">
    <!-- ── Header: Stats | Uhrzeit | Gesamtstrecke ── -->
    <div class="header">
      <div class="hd-stats">
        <div class="hd-stat">
          <span class="hd-stat-val">{{ totalCount }}</span>
          <span class="hd-stat-lbl">Teilnehmer</span>
        </div>
        <div class="hd-divider"></div>
        <div class="hd-stat">
          <span class="hd-stat-val hd-active-val">
            <span class="active-dot"></span>{{ activeCount }}
          </span>
          <span class="hd-stat-lbl">Im Wasser</span>
        </div>
      </div>

      <div class="hd-total">
        <span class="hd-total-val">{{ fmtTotal }}</span>
      </div>

      <div class="hd-time">{{ currentTime }}</div>
    </div>

    <!-- ── Verbindungsfehler ── -->
    <div v-if="error" class="conn-err">
      Verbindungsfehler – letzte Daten werden angezeigt
    </div>

    <!-- ── Ladeindikator ── -->
    <div v-if="sorted.length === 0 && !error" class="loading">
      Lade Ergebnisse…
    </div>

    <!-- ── Ergebnisliste ── -->
    <div v-else ref="listSect" class="list-sect">

      <!-- Modus: Scrollen (Standard, ?scroll=off fehlt) -->
      <template v-if="scrollMode">
        <div
          ref="scrollWrap"
          class="scroll-wrap"
          :class="{ 'is-scrolling': needsScroll }"
          :style="needsScroll ? { animationDuration: scrollDuration + 's' } : {}"
        >
          <div class="list-block">
            <div v-for="r in sorted" :key="r.id" class="list-row" :class="{ 'row-active': r.isActive }">
              <span class="lr-dot" :class="{ 'lr-dot-on': r.isActive }"></span>
              <span class="lr-id">{{ r.id }}</span>
              <span class="lr-name">{{ r.firstName }} {{ r.lastName }}</span>
              <span class="lr-dist">{{ fmtDist(r.lanes) }}</span>
            </div>
          </div>
          <div v-if="needsScroll" class="list-block" aria-hidden="true">
            <div v-for="r in sorted" :key="'dup-' + r.id" class="list-row" :class="{ 'row-active': r.isActive }">
              <span class="lr-dot" :class="{ 'lr-dot-on': r.isActive }"></span>
              <span class="lr-id">{{ r.id }}</span>
              <span class="lr-name">{{ r.firstName }} {{ r.lastName }}</span>
              <span class="lr-dist">{{ fmtDist(r.lanes) }}</span>
            </div>
          </div>
        </div>
      </template>

      <!-- Modus: Seiten blättern (?scroll=off) -->
      <template v-else>
        <div class="list-block">
          <div v-for="r in visibleItems" :key="r.id" class="list-row" :class="{ 'row-active': r.isActive }">
            <span class="lr-dot" :class="{ 'lr-dot-on': r.isActive }"></span>
            <span class="lr-id">{{ r.id }}</span>
            <span class="lr-name">{{ r.firstName }} {{ r.lastName }}</span>
            <span class="lr-dist">{{ fmtDist(r.lanes) }}</span>
          </div>
        </div>
        <div v-if="totalPages > 1" class="page-indicator">
          {{ pageIndex + 1 }} / {{ totalPages }}
        </div>
      </template>

    </div>
  </section>
</template>

<script>
import { participantService } from '../services/participantService.js'
import TheHeader from '../components/UI/TheHeader.vue'

const PER_ITEM_S = 1
export default {
  name: 'Results3',

  components: { TheHeader },

  data() {
    return {
      raw:          [],
      error:        null,
      currentTime:  new Date().toLocaleTimeString(),
      navOpen:          false,
      needsScroll:      false,
      pageIndex:        0,
      itemsPerPage:     20,
      _lastMouseCheck:  0,
    }
  },

  computed: {
    scrollMode() {
      return this.$route?.query?.scroll !== 'off'
    },
    sorted() {
      return this.raw.slice().sort((a, b) => (a.id || 0) - (b.id || 0))
    },
    totalCount() {
      return this.raw.length
    },
    activeCount() {
      return this.raw.filter(r => r.isActive).length
    },
    fmtTotal() {
      return this.fmtDist(this.raw.reduce((s, r) => s + (r.lanes || 0), 0))
    },
    scrollDuration() {
      return Math.max(this.sorted.length * PER_ITEM_S, 10)
    },
    totalPages() {
      return Math.max(1, Math.ceil(this.sorted.length / this.itemsPerPage))
    },
    visibleItems() {
      const start = this.pageIndex * this.itemsPerPage
      return this.sorted.slice(start, start + this.itemsPerPage)
    },
  },

  methods: {
    fmtDist(lanes) {
      if (lanes == null) return 'N/A'
      const m = lanes * 25
      return m > 1000
        ? (m / 1000).toLocaleString('de-DE') + ' km'
        : m.toLocaleString('de-DE') + ' m'
    },
    async load() {
      try {
        const fresh = await participantService.searchAsYouType('')
        this.error = null
        if (!this._dataEqual(fresh, this.raw)) {
          this.raw = fresh.map(item => Object.freeze(item))
          if (this.scrollMode) {
            await this.checkScroll()
          } else {
            await this.$nextTick()
            this.calculateItemsPerPage()
          }
        }
      } catch (e) {
        this.error = e.message
      }
    },
    _dataEqual(a, b) {
      if (a.length !== b.length) return false
      return a.every((item, i) =>
        item.id === b[i].id &&
        item.lanes === b[i].lanes &&
        item.isActive === b[i].isActive
      )
    },
    async checkScroll() {
      // Immer in 1-Spalten-Modus messen, sonst Oszillation
      this.needsScroll = false
      await this.$nextTick()
      const sect = this.$refs.listSect
      const wrap = this.$refs.scrollWrap
      if (!sect || !wrap) return
      this.needsScroll = wrap.scrollHeight > sect.clientHeight
    },
    advancePage() {
      this.pageIndex = (this.pageIndex + 1) % this.totalPages
    },
    calculateItemsPerPage() {
      const sect = this.$refs.listSect
      if (!sect) return
      const row = sect.querySelector('.list-row')
      const rowH = row ? row.getBoundingClientRect().height : 32
      const fit = Math.floor(sect.clientHeight / rowH)
      const cols = window.innerWidth > 900 ? 2 : 1
      if (fit > 0) this.itemsPerPage = fit * cols
    },
    onMouseMove(e) {
      const now = Date.now()
      if (now - this._lastMouseCheck < 100) return
      this._lastMouseCheck = now
      if (e.clientY < 60) {
        clearTimeout(this._hideNavTimer)
        this.navOpen = true
      } else if (this.navOpen) {
        clearTimeout(this._hideNavTimer)
        this._hideNavTimer = setTimeout(() => { this.navOpen = false }, 800)
      }
    },
  },

  mounted() {
    this.load()
    this._pollId  = setInterval(this.load, 30_000)
    this._clockId = setInterval(() => { this.currentTime = new Date().toLocaleTimeString() }, 1000)
    document.addEventListener('mousemove', this.onMouseMove)

    if (this.scrollMode) {
      this._resizeObserver = new ResizeObserver(this.checkScroll)
      this.$nextTick(() => {
        if (this.$refs.listSect) this._resizeObserver.observe(this.$refs.listSect)
      })
    } else {
      this._pageTimer = setInterval(this.advancePage, 10_000)
      this.$nextTick(() => this.calculateItemsPerPage())
      this._resizeObserver = new ResizeObserver(() => {
        this.calculateItemsPerPage()
        this.pageIndex = 0
      })
      this.$nextTick(() => {
        if (this.$refs.listSect) this._resizeObserver.observe(this.$refs.listSect)
      })
    }
  },

  beforeUnmount() {
    clearInterval(this._pollId)
    clearInterval(this._clockId)
    clearInterval(this._pageTimer)
    clearTimeout(this._hideNavTimer)
    document.removeEventListener('mousemove', this.onMouseMove)
    if (this._resizeObserver) this._resizeObserver.disconnect()
  },
}
</script>

<style scoped>
/* ── Grundstruktur ─────────────────────────────────────────────────────────── */

.tv-screen {
  position: fixed;
  inset: 0;
  background: #111;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: Arial, sans-serif;
}

/* ── Header ────────────────────────────────────────────────────────────────── */

.header {
  flex: 0 0 auto;
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 0.5em;
  padding: 0.15em 0.6em;
  background: #bf0f0f;
  color: #fff;
  z-index: 100;
}

/* Linke Seite: Teilnehmer + Im Wasser */
.hd-stats {
  display: flex;
  align-items: center;
  gap: 0.6em;
  justify-content: flex-start;
}

.hd-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.1;
}

.hd-stat-val {
  font-size: clamp(1.1rem, 3.2vw, 3.5rem);
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 0.25em;
}

.hd-stat-lbl {
  font-size: clamp(0.5rem, 1.2vw, 1.3rem);
  color: rgba(255, 255, 255, 0.9);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.hd-divider {
  width: 1px;
  height: 2em;
  background: rgba(255,255,255,0.35);
  flex: 0 0 auto;
}

/* Pulsierender grüner Punkt im "Im Wasser"-Wert */
.active-dot {
  display: inline-block;
  width: 0.55em;
  height: 0.55em;
  border-radius: 50%;
  background: #4caf50;
  box-shadow: 0 0 0.35em #4caf50;
  animation: dot-pulse 1.8s ease-in-out infinite;
  flex-shrink: 0;
}

@keyframes dot-pulse {
  0%, 100% { opacity: 1;   transform: scale(1);   }
  50%       { opacity: 0.5; transform: scale(1.25); }
}

/* Mitte: Gesamtstrecke */
.hd-total {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.1;
}


.hd-total-val {
  font-size: clamp(1.75rem, 6.5vw, 7rem);
  font-weight: bold;
  white-space: nowrap;
}

/* Rechte Seite: Uhrzeit */
.hd-time {
  font-size: clamp(1.1rem, 3.2vw, 3.5rem);
  font-weight: bold;
  text-align: right;
  white-space: nowrap;
  line-height: 1.1;
}

/* ── Fehler / Laden ────────────────────────────────────────────────────────── */

.conn-err {
  flex: 0 0 auto;
  text-align: center;
  padding: 0.3em;
  background: #bf360c;
  color: #fff;
  font-size: clamp(0.75rem, 1.5vw, 1.25rem);
}

.loading {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: clamp(1rem, 2vw, 2rem);
}

/* ── Scrollende Liste ──────────────────────────────────────────────────────── */

.list-sect {
  flex: 1;
  min-height: 0;
  overflow: hidden;
  position: relative;
  font-size: clamp(0.9rem, 2.4vw, 2.65rem);
  color: #e0e0e0;
}

.scroll-wrap {
  will-change: transform;
}

.scroll-wrap.is-scrolling {
  animation: scroll-up linear infinite;
}

.list-block {
  width: 100%;
}

.list-row {
  display: flex;
  align-items: center;
  padding: 0.08em 0.6em;
  gap: 0.4em;
  white-space: nowrap;
  box-sizing: border-box;
  color: #e0e0e0;
  border-left: 3px solid transparent;
}

.list-row.row-active {
  background: rgba(76, 175, 80, 0.07);
  border-left-color: #4caf50;
}

/* Status-Punkt pro Zeile */
.lr-dot {
  flex: 0 0 0.5em;
  width: 0.5em;
  height: 0.5em;
  border-radius: 50%;
  background: transparent;
  transition: background 0.3s, box-shadow 0.3s;
}

.lr-dot.lr-dot-on {
  background: #4caf50;
  box-shadow: 0 0 0.35em #4caf50;
}

.lr-id {
  flex: 0 0 2.4em;
  text-align: right;
  color: #888;
  font-size: 0.85em;
}

.lr-name {
  flex: 0 0 16em;
  overflow: hidden;
  text-overflow: ellipsis;
}

.lr-dist {
  flex: 0 0 6.5em;
  text-align: right;
}

@keyframes scroll-up {
  from { transform: translateY(0); }
  to   { transform: translateY(-50%); }
}

.page-indicator {
  position: absolute;
  bottom: 0.4em;
  right: 0.6em;
  font-size: clamp(0.6rem, 1vw, 1rem);
  color: #555;
}

/* ── TV / Desktop: 2 Spalten ───────────────────────────────────────────────── */

@media (min-width: 901px) {
  .list-block {
    display: grid;
    grid-template-columns: 1fr 1fr;
  }
  .list-row {
    min-width: 0;
    overflow: hidden;
  }
  .lr-name {
    flex: 1;
    min-width: 0;
  }
  .lr-dist {
    flex: 0 0 5em;
  }
}

/* ── Tablet ────────────────────────────────────────────────────────────────── */

@media (max-width: 900px) {
  .hd-stat-lbl {
    display: none;
  }
  .list-sect {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .scroll-wrap,
  .list-block {
    width: 100%;
    max-width: 680px;
  }
}

/* ── Mobile ────────────────────────────────────────────────────────────────── */

@media (max-width: 600px) {
  .header {
    grid-template-columns: 1fr auto;
    grid-template-rows: auto auto;
  }

  .hd-stats {
    grid-column: 1;
    grid-row: 2;
    justify-content: flex-start;
    padding-left: 0.2em;
  }

  .hd-total {
    grid-column: 2;
    grid-row: 1 / 3;
    align-items: flex-end;
  }

  .hd-time {
    grid-column: 1;
    grid-row: 1;
    text-align: left;
  }

  .lr-name {
    flex: 1;
    min-width: 0;
  }
}

</style>
