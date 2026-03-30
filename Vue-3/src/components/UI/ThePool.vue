<template>
  <v-row class="pool-container flex-nowrap" no-gutters>
    <v-col v-if="counterPosition === 'left'" class="counter counter-left">Zähler</v-col>
    <v-col
        class="pool"
        :class="{ 'scaled-pattern': poolSize >= 50 }"
        :style="patternVars"
    >
      <div v-for="lane in lanes" :key="lane" class="lane-intersection">
        <div class="lane">
          <router-link :to="'/lane/' + lane">Bahn {{ lane }}</router-link>
          <div class="lane-mark" aria-hidden="true"></div>
        </div>
      </div>
    </v-col>
    <v-col cols="auto" v-if="counterPosition === 'right'" class="counter counter-right pa-2">Zähler</v-col>
  </v-row>
</template>

<script>
export default {
  name: 'ThePool',
  props: {
    laneCount: {
      type: Number,
      required: true
    },
    counterPosition: {
      type: String,
      required: true
    },
    reverseLanes: {
      type: Boolean,
      default: false
    },
    poolSize: {
      type: Number,
      default: 25,
    }
  },
  computed: {
    lanes() {
      const lanesArray = Array.from({length: this.laneCount}, (_, i) => i + 1);
      return this.reverseLanes ? lanesArray.reverse() : lanesArray;
    },
    patternVars() {
      // Nur setzen, wenn poolSize >= 50
      if (this.poolSize < 50) return {};
      const segMeters = [5, 17, 6, 17, 5];
      const total = this.poolSize;
      const perc = segMeters.map(s => (s / total * 100).toFixed(6) + '%');
      return {
        '--s1': perc[0],
        '--s2': perc[1],
        '--s3': perc[2],
        '--s4': perc[3],
        '--s5': perc[4],
      };
    }
  }
}
</script>

<style scoped>
.pool-container {
  display: flex;
  align-items: stretch;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
  padding: 0.2rem;
  margin: 2rem auto;
  background: gray;
}

.pool {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #72ABD8;
  padding: 0;
  border: 10px solid gray;
  position: relative;
  flex-grow: 1;
}

/* Standard-Muster (bestehendes Verhalten) */
.lane-intersection {
  width: 100%;
  height: 80px;
  text-align: center;
  margin: 0;
  padding: 0;
  border: 0;
  position: relative;
}

.lane-intersection:not(:last-child)::after {
  content: '';
  display: block;
  width: 100%;
  height: 10px;
  position: absolute;
  bottom: -5px;
  left: 0;
  background: linear-gradient(90deg, red 0 20%, rgba(255, 0, 0, 0) 20% 80%, red 80% 100%),
  repeating-linear-gradient(90deg, #0046b5 0 10px, #ffffff 10px 20px);
  background-repeat: no-repeat, repeat;
  background-size: 100% 100%, auto;
}

/* Skalierbares Muster für poolSize >= 50
   Segment-Reihenfolge (Meter): 5m rot, 15m blau/weiß, 10m rot, 15m blau/weiß, 5m rot
   Die Prozentwerte werden per CSS-Variablen gesetzt (--s1 ... --s5).
   Fallbacks verhindern IDE-Warnungen, falls die Variablen nicht zur Analysezeit gesetzt sind.
*/
.pool.scaled-pattern .lane-intersection:not(:last-child)::after {
  content: '';
  display: block;
  width: 100%;
  height: 10px;
  position: absolute;
  bottom: -5px;
  left: 0;

  background: linear-gradient(90deg,
  red 0 var(--s1, 10%),
  transparent var(--s1, 10%) calc(var(--s1, 10%) + var(--s2, 34%)),
  red calc(var(--s1, 10%) + var(--s2, 34%)) calc(var(--s1, 10%) + var(--s2, 34%) + var(--s3, 12%)),
  transparent calc(var(--s1, 10%) + var(--s2, 34%) + var(--s3, 12%)) calc(var(--s1, 10%) + var(--s2, 34%) + var(--s3, 12%) + var(--s4, 34%)),
  red calc(var(--s1, 10%) + var(--s2, 34%) + var(--s3, 12%) + var(--s4, 34%)) 100%
  ),
  repeating-linear-gradient(90deg, #0046b5 0 10px, #ffffff 10px 20px),
  repeating-linear-gradient(90deg, #0046b5 0 10px, #ffffff 10px 20px);

  background-repeat: no-repeat, repeat, repeat;
  background-size: 100% 100%, var(--s2, 34%) 100%, var(--s4, 34%) 100%;
  background-position: 0 0, var(--s1, 10%) 0, calc(var(--s1, 10%) + var(--s2, 34%) + var(--s3, 12%)) 0;
}

/* Bodenmarkierung (Mittel-Linie mit T-Enden)
   - Abstand zu beiden Poolwänden: 30px (left/right)
   - Strich zentriert in der Bahn, leicht dunkleres Blau
   - T-Enden als senkrechte Balken an den Enden der Linie
*/
.lane {
  width: 100%;
  text-align: center;
  background-color: #72ABD8;
  padding: 10px;
  position: relative;
  overflow: visible;
}

/* Die eigentliche Linienleiste */
.lane-mark {
  position: absolute;
  left: 30px; /* 30px Abstand zur linken Wand */
  right: 30px; /* 30px Abstand zur rechten Wand */
  height: 10px; /* sichtbare Breite der Linie */
  top: 50%; /* vertikal zentriert in der Bahn */
  transform: translateY(-50%);
  background-color: rgb(78, 151, 207); /* leicht dunkleres Blau */
  z-index: 0;
  border-radius: 2px;
  pointer-events: none;
}

/* T-Enden (senkrechte Balken an beiden Enden der Linie) */
.lane-mark::before,
.lane-mark::after {
  content: '';
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 10px; /* Breite des Querbalkens (T-Stamm) */
  height: 50px; /* Länge des Querbalkens (T-Kopf Länge) */
  background-color: rgb(78, 151, 207);
  border-radius: 2px;
  pointer-events: none;
}

.lane-mark::before {
  left: 0;
}

.lane-mark::after {
  right: 0;
}

/* Link-Text über der Markierung halten */
.lane a {
  position: relative;
  z-index: 2;
  text-decoration: none;
  color: #013157;
  font-weight: bold;
  font-size: 2em;
}

/* Anpassungen für den Zähler: keine Wachstumseigenschaft, feste Max-/Min-Breite */
.counter {
  writing-mode: vertical-rl;
  text-orientation: mixed;
  text-decoration: none;
  color: #013157;
  font-weight: bold;
  font-size: 2em;
  transform: rotate(180deg);

  background-color: #ffb9b9;
  padding: 10px;
  border-top: 10px solid gray;
  border-bottom: 10px solid gray;
  display: flex;
  align-items: center;
  justify-content: center;

  flex: 0 0 auto; /* verhindert Aufweiten */
  width: 80px; /* gewünschte dünne Leiste */
  min-width: 40px;
  max-width: 100px;
  box-sizing: border-box;
}

.counter-left {
  border-right: 10px solid gray;
}

.counter-right {
  border-left: 10px solid gray;
}

/* Optional: kleinere Schrift auf sehr kleinen Bildschirmen */
@media (max-width: 500px) {
  .lane a {
    font-size: 2em;
  }

  .counter {
    font-size: 1.5em !important;
    width: 45px;
    min-width: 32px;
  }
}
</style>