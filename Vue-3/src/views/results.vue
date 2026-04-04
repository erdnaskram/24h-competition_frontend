<template>
  <section class="tv-screen">
    <div id="count">
      {{ currentTime }} - {{ formatDistance(laneSumm) }}
    </div>

    <div id="resultcontainer">
      <div
          class="scroll-wrapper"
          :style="{ animationDuration: scrollDuration + 's' }"
      >

        <div class="results-block">
          <div v-for="result in results" :key="'orig-' + result.id" class="result">
            #{{ result.id }} - {{ result.first_name }} {{ result.last_name }}: {{ formatDistance(result.lanes) }}
          </div>
        </div>

        <div class="results-block">
          <div v-for="result in results" :key="'copy-' + result.id" class="result">
            #{{ result.id }} - {{ result.first_name }} {{ result.last_name }}: {{ formatDistance(result.lanes) }}
          </div>
        </div>

      </div>
    </div>
  </section>
</template>

<script>
// Umgerechnet in Sekunden für die CSS-Animation (800ms = 0.8s pro Element)
const SCROLL_FACTOR_SECONDS = 0.8;

export default {
  name: 'Results',
  data() {
    return {
      count: 0,
      currentTime: new Date().toLocaleTimeString(),
      baseValue: 0,
      results:[
          {
        "id": 1,
        "first_name": "Blondie",
        "last_name": "Brinkman",
        "lanes": 28
      }, {
        "id": 2,
        "first_name": "Lemmy",
        "last_name": "Eddoes",
        "lanes": 82
      }, {
        "id": 3,
        "first_name": "Jacklyn",
        "last_name": "Sebright",
        "lanes": 106
      }, {
        "id": 4,
        "first_name": "Florian",
        "last_name": "Cream",
        "lanes": 95
      }, {
        "id": 5,
        "first_name": "Lenci",
        "last_name": "Pelz",
        "lanes": 197
      }, {
        "id": 6,
        "first_name": "Otis",
        "last_name": "O'Cleary",
        "lanes": 169
      }, {
        "id": 7,
        "first_name": "Phelia",
        "last_name": "Crinkley",
        "lanes": 26
      }, {
        "id": 8,
        "first_name": "Kiley",
        "last_name": "Gilchriest",
        "lanes": 51
      }, {
        "id": 9,
        "first_name": "Marion",
        "last_name": "Batman",
        "lanes": 96
      }, {
        "id": 10,
        "first_name": "Dom",
        "last_name": "Lascelles",
        "lanes": 104
      }, {
        "id": 11,
        "first_name": "Retha",
        "last_name": "Mapowder",
        "lanes": 82
      }, {
        "id": 12,
        "first_name": "Cathryn",
        "last_name": "Morrad",
        "lanes": 199
      }, {
        "id": 13,
        "first_name": "Virgie",
        "last_name": "Robilliard",
        "lanes": 184
      }, {
        "id": 14,
        "first_name": "Lelia",
        "last_name": "Pantry",
        "lanes": 178
      }, {
        "id": 15,
        "first_name": "Shelagh",
        "last_name": "Fairman",
        "lanes": null
      }, {
        "id": 16,
        "first_name": "Kalil",
        "last_name": "Ughi",
        "lanes": 113
      }, {
        "id": 17,
        "first_name": "Morton",
        "last_name": "Dufton",
        "lanes": 163
      }, {
        "id": 18,
        "first_name": "Jenica",
        "last_name": "Bernini",
        "lanes": 48
      }, {
        "id": 19,
        "first_name": "Georgiana",
        "last_name": "Butfield",
        "lanes": 144
      }, {
        "id": 20,
        "first_name": "Mal",
        "last_name": "Sheriff",
        "lanes": 103
      }, {
        "id": 21,
        "first_name": "Humfrid",
        "last_name": "Rizzolo",
        "lanes": 165
      }, {
        "id": 22,
        "first_name": "Baldwin",
        "last_name": "Curtiss",
        "lanes": 121
      }, {
        "id": 23,
        "first_name": "Garik",
        "last_name": "Boow",
        "lanes": 161
      }, {
        "id": 24,
        "first_name": "Della",
        "last_name": "Amar",
        "lanes": 163
      }, {
        "id": 25,
        "first_name": "Etan",
        "last_name": "Edelheid",
        "lanes": 157
      }, {
        "id": 26,
        "first_name": "George",
        "last_name": "Hayhurst",
        "lanes": 184
      }, {
        "id": 27,
        "first_name": "Brunhilde",
        "last_name": "Lorenzetto",
        "lanes": 174
      }, {
        "id": 28,
        "first_name": "Michaeline",
        "last_name": "Fury",
        "lanes": 126
      }, {
        "id": 29,
        "first_name": "Cookie",
        "last_name": "Paszek",
        "lanes": 118
      }, {
        "id": 30,
        "first_name": "Gertrud",
        "last_name": "Hardwidge",
        "lanes": 8
      }, {
        "id": 31,
        "first_name": "Redford",
        "last_name": "Lardeur",
        "lanes": 4
      }, {
        "id": 32,
        "first_name": "Reeva",
        "last_name": "Hallin",
        "lanes": 86
      }, {
        "id": 33,
        "first_name": "Bobbye",
        "last_name": "Walduck",
        "lanes": 141
      }, {
        "id": 34,
        "first_name": "Shel",
        "last_name": "Husby",
        "lanes": 54
      }, {
        "id": 35,
        "first_name": "Barbra",
        "last_name": "Ings",
        "lanes": 66
      }, {
        "id": 36,
        "first_name": "Happy",
        "last_name": "Olorenshaw",
        "lanes": 96
      }, {
        "id": 37,
        "first_name": "Erwin",
        "last_name": "Aguirrezabala",
        "lanes": 96
      }, {
        "id": 38,
        "first_name": "Arabela",
        "last_name": "Braven",
        "lanes": 164
      }, {
        "id": 39,
        "first_name": "Jaimie",
        "last_name": "Lewerenz",
        "lanes": 4
      }, {
        "id": 40,
        "first_name": "Ulrike",
        "last_name": "Stollman",
        "lanes": 11
      }, {
        "id": 41,
        "first_name": "Clemmie",
        "last_name": "Rayer",
        "lanes": 92
      }, {
        "id": 42,
        "first_name": "Colan",
        "last_name": "Mersey",
        "lanes": 177
      }, {
        "id": 43,
        "first_name": "Kelley",
        "last_name": "Babb",
        "lanes": 9
      }, {
        "id": 44,
        "first_name": "Vitoria",
        "last_name": "Hairsnape",
        "lanes": 54
      }, {
        "id": 45,
        "first_name": "Katheryn",
        "last_name": "Gajewski",
        "lanes": 10
      }, {
        "id": 46,
        "first_name": "Kennie",
        "last_name": "Leblanc",
        "lanes": 62
      }, {
        "id": 47,
        "first_name": "Lynsey",
        "last_name": "Proom",
        "lanes": 158
      }, {
        "id": 48,
        "first_name": "Chandler",
        "last_name": "Itzakovitz",
        "lanes": 199
      }, {
        "id": 49,
        "first_name": "Judye",
        "last_name": "Treece",
        "lanes": 199
      }, {
        "id": 50,
        "first_name": "Lazar",
        "last_name": "Gatsby",
        "lanes": 133
      }, {
        "id": 51,
        "first_name": "Melessa",
        "last_name": "Fentem",
        "lanes": 8
      }, {
        "id": 52,
        "first_name": "Lowe",
        "last_name": "Davana",
        "lanes": 185
      }, {
        "id": 53,
        "first_name": "Kellen",
        "last_name": "Shirley",
        "lanes": 61
      }, {
        "id": 54,
        "first_name": "Aldin",
        "last_name": "Pottle",
        "lanes": 137
      }, {
        "id": 55,
        "first_name": "Nollie",
        "last_name": "Rennicks",
        "lanes": 22
      }, {
        "id": 56,
        "first_name": "Ward",
        "last_name": "Espinho",
        "lanes": 135
      }, {
        "id": 57,
        "first_name": "Gayle",
        "last_name": "Clendennen",
        "lanes": 10
      }, {
        "id": 58,
        "first_name": "Muhammad",
        "last_name": "Norris",
        "lanes": 32
      }, {
        "id": 59,
        "first_name": "Corine",
        "last_name": "Papis",
        "lanes": 72
      }, {
        "id": 60,
        "first_name": "Coralyn",
        "last_name": "McReynold",
        "lanes": 182
      }, {
        "id": 61,
        "first_name": "Ceciley",
        "last_name": "Georgins",
        "lanes": 61
      }, {
        "id": 62,
        "first_name": "Daisy",
        "last_name": "Fust",
        "lanes": 143
      }, {
        "id": 63,
        "first_name": "Raine",
        "last_name": "Algar",
        "lanes": 10
      }, {
        "id": 64,
        "first_name": "Culley",
        "last_name": "Trimmill",
        "lanes": 39
      }, {
        "id": 65,
        "first_name": "Kev",
        "last_name": "Cursons",
        "lanes": 75
      }, {
        "id": 66,
        "first_name": "Charlean",
        "last_name": "Yetts",
        "lanes": 121
      }, {
        "id": 67,
        "first_name": "Ricca",
        "last_name": "Dymock",
        "lanes": 102
      }, {
        "id": 68,
        "first_name": "Kiele",
        "last_name": "Fidele",
        "lanes": 126
      }, {
        "id": 69,
        "first_name": "David",
        "last_name": "Whotton",
        "lanes": 183
      }, {
        "id": 70,
        "first_name": "Nikkie",
        "last_name": "Briggdale",
        "lanes": 108
      }, {
        "id": 71,
        "first_name": "Halley",
        "last_name": "Acaster",
        "lanes": 54
      }, {
        "id": 72,
        "first_name": "Malissia",
        "last_name": "O'Shevlin",
        "lanes": 119
      }, {
        "id": 73,
        "first_name": "Orran",
        "last_name": "Nuton",
        "lanes": 90
      }, {
        "id": 74,
        "first_name": "Waite",
        "last_name": "Featley",
        "lanes": 88
      }, {
        "id": 75,
        "first_name": "Glennis",
        "last_name": "Danielli",
        "lanes": 122
      }, {
        "id": 76,
        "first_name": "Dulcea",
        "last_name": "Ennever",
        "lanes": 170
      }, {
        "id": 77,
        "first_name": "Sig",
        "last_name": "Farres",
        "lanes": 24
      }, {
        "id": 78,
        "first_name": "Nevsa",
        "last_name": "Olivetti",
        "lanes": 184
      }, {
        "id": 79,
        "first_name": "Dael",
        "last_name": "Huccaby",
        "lanes": 113
      }, {
        "id": 80,
        "first_name": "Haily",
        "last_name": "Edwin",
        "lanes": 14
      }, {
        "id": 81,
        "first_name": "Kendal",
        "last_name": "Mulliss",
        "lanes": 94
      }, {
        "id": 82,
        "first_name": "Billy",
        "last_name": "Tapton",
        "lanes": 56
      }, {
        "id": 83,
        "first_name": "Hartwell",
        "last_name": "Funcheon",
        "lanes": 22
      }, {
        "id": 84,
        "first_name": "Kimbell",
        "last_name": "Figgen",
        "lanes": 144
      }, {
        "id": 85,
        "first_name": "Nelia",
        "last_name": "Hindrich",
        "lanes": 119
      }, {
        "id": 86,
        "first_name": "Cinnamon",
        "last_name": "Gasperi",
        "lanes": 49
      }, {
        "id": 87,
        "first_name": "Adams",
        "last_name": "Maddison",
        "lanes": 125
      }, {
        "id": 88,
        "first_name": "Giorgia",
        "last_name": "Whylie",
        "lanes": 8
      }, {
        "id": 89,
        "first_name": "Eartha",
        "last_name": "Boxell",
        "lanes": 200
      }, {
        "id": 90,
        "first_name": "Cody",
        "last_name": "Carek",
        "lanes": 73
      }, {
        "id": 91,
        "first_name": "Ted",
        "last_name": "Comizzoli",
        "lanes": 40
      }, {
        "id": 92,
        "first_name": "Liva",
        "last_name": "Peres",
        "lanes": 54
      }, {
        "id": 93,
        "first_name": "Eolande",
        "last_name": "Duberry",
        "lanes": 180
      }, {
        "id": 94,
        "first_name": "Rebecca",
        "last_name": "Ginnally",
        "lanes": 171
      }, {
        "id": 95,
        "first_name": "Gregory",
        "last_name": "Northway",
        "lanes": 49
      }, {
        "id": 96,
        "first_name": "Marvin",
        "last_name": "Summerill",
        "lanes": 111
      }, {
        "id": 97,
        "first_name": "Janeen",
        "last_name": "Revely",
        "lanes": 11
      }, {
        "id": 98,
        "first_name": "Rhianna",
        "last_name": "Escott",
        "lanes": 53
      }, {
        "id": 99,
        "first_name": "Matt",
        "last_name": "Vallow",
        "lanes": 57
      }, {
        "id": 100,
        "first_name": "Flore",
        "last_name": "D'Onise",
        "lanes": 24
      }]
    }
  },
  computed: {
    laneSumm() {
      return this.results.reduce((sum, result) => sum + (result.lanes || 0), 0);
    },
    // Berechnet die Dauer der Animation abhängig von der Anzahl der Teilnehmer
    scrollDuration() {
      return this.results.length * SCROLL_FACTOR_SECONDS;
    }
  },
  methods: {
    updateTime() {
      this.currentTime = new Date().toLocaleTimeString();
    },
    formatDistance(distance) {
      if (distance === null || distance === undefined) {
        return 'N/A';
      }
      let meters = distance * 25;
      if (meters >= 1000) {
        return (meters / 1000).toFixed(2) + ' km';
      } else {
        return meters + ' m';
      }
    },
  },
  mounted() {
    this.timer = setInterval(this.updateTime, 1000);

    this.timer2 = setInterval(() => {
      this.baseValue += Math.floor(Math.random() * (500 - 50 + 1)) + 50;
    }, 5000);
  },
  // WICHTIG: In Vue 3 heißt das beforeUnmount (nicht mehr beforeDestroy)
  beforeUnmount() {
    clearInterval(this.timer);
    clearInterval(this.timer2);
  }
}
</script>

<style scoped>
.tv-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: black;
  overflow: hidden;
  z-index: 10;
}

#count {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  text-align: center;
  /* fluid: ~28px auf 375px Handy, ~80px auf 1200px, 120px auf 1800px+ TV */
  font-size: clamp(1.75rem, 7vw, 7.5rem);
  line-height: 1.15;
  padding: 0.1em 0;
  color: white;
  font-family: Arial;
  font-weight: bold;
  background-color: #bf0f0f;
  z-index: 100;
}

#resultcontainer {
  background-color: black;
  position: absolute;
  /* passt sich an die Höhe des #count-Headers an (ca. 1.35 × font-size) */
  top: clamp(50px, 9.5vw, 160px);
  left: 0;
  bottom: 0;
  right: 0;
  overflow: hidden;
}

.scroll-wrapper {
  animation-name: scroll-vertical;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  /* fluid: ~16px auf 375px Handy, ~42px auf 1200px, 60px auf 1714px+ TV */
  font-size: clamp(1rem, 3.5vw, 3.75rem);
  color: white;
  font-family: Arial;
}

.results-block {
  width: 100%;
}

.result {
  display: block;
  width: 100%;
  padding: 0.05em 0.5em;
  box-sizing: border-box;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Ab 1200px (Desktop / TV): Mehrspaltiges Layout wie vorher */
@media (min-width: 1200px) {
  .result {
    display: inline-block;
    width: auto;
    min-width: 35em;
    padding-left: 25px;
    overflow: visible;
    text-overflow: unset;
    white-space: nowrap;
  }
}

@keyframes scroll-vertical {
  0%   { transform: translateY(0); }
  100% { transform: translateY(-50%); }
}
</style>