<template>
  <v-dialog v-model="showModal"
            max-width="1000"
            persistent>
    <v-card title="Berabeiten">
      <v-card-text>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Badekleidung</p>
            <v-btn-toggle v-model="swimSuit" border divided>
              <v-btn
                  class="characteristics-btn">
                <bikini-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
              <v-btn
                  class="characteristics-btn">
                <swim-suit-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
              <v-btn
                  class="characteristics-btn">
                <short-pants-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
              <v-btn
                  class="characteristics-btn">
                <pants-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
            </v-btn-toggle>
          </v-col>
          <v-col
              class="py-2"
          >
            <p>Farbe der Badekleidung</p>
            <v-btn-toggle v-model="swimsuitColour" border divided>
              <v-btn
                  class="characteristics-btn"
                  v-for="color in colorOptions"
                  :key="color"
                  :value="color"
              >
                <v-card :class="'bg-' + color"
                        border="accent thin"
                        style="width: 40px; height: 40px; border-radius: 10%;">

                </v-card>
              </v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Schwimmbrille</p>
            <v-btn-toggle v-model="googles" border divided>
              <v-btn>
                <goggles-icon
                    class="swimmer-characteristics-icon char-purp"
                    :style="{fill: `rgb(var(--v-theme-purple-accent-4)`}"/>
                <v-icon
                    style="position: absolute; top: 35%; left: 65%; transform: translate(-50%, -50%);"
                    size="40"
                    color="red"
                >
                  mdi-close-thick
                </v-icon>
              </v-btn>
              <v-btn
                  class="characteristics-btn"
                  v-for="color in colorMap"
                  :key="color.id"
                  :value="color.id"
              >
                <goggles-icon
                    class="swimmer-characteristics-icon"
                    :class="{'icon-border-googles': color.id === 'white' }"
                    :style="{ fill: `rgb(var(--v-theme-${color.cssVar}))` }"/>
              </v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Badekappe</p>
            <v-btn-toggle v-model="cap" border divided>
              <v-btn>
                <hat-icon
                    class="swimmer-characteristics-icon"/>
                <v-icon
                    style="position: absolute; top: 35%; left: 65%; transform: translate(-50%, -50%);"
                    size="40"
                    color="red"
                >
                  mdi-close-thick
                </v-icon>
              </v-btn>
              <v-btn
                  class="characteristics-btn"
                  v-for="color in colorMap"
                  :key="color.id"
                  :value="color.id"
              >
                <hat-icon
                    class="swimmer-characteristics-icon"
                    :class="{'icon-border-hat': color.id === 'white' }"
                    :style="{ fill: `rgb(var(--v-theme-${color.cssVar}))` }"/>
              </v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Haarfarbe</p>
            <v-btn-toggle v-model="hair" border divided>
              <v-btn>
                <hair-icon
                    class="swimmer-characteristics-icon"/>
                <v-icon
                    style="position: absolute; top: 35%; left: 65%; transform: translate(-50%, -50%);"
                    size="40"
                    color="red"
                >
                  mdi-close-thick
                </v-icon>
              </v-btn>
              <v-btn
                  class="characteristics-btn"
                  v-for="color in hairColorMap"
                  :key="color.id"
                  :value="color.id"
              >
                <hair-icon
                    class="swimmer-characteristics-icon"
                    :class="{'icon-border-hair': color.id === 'white' }"
                    :style="{ color: `rgb(var(--v-theme-${color.cssVar}))` }"/>
              </v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col sm="4" md="2">
            <p>Tatoo</p>
            <v-btn-toggle v-model="tatoo" border
                          divided>
              <v-btn>
                Ja
              </v-btn>
              <v-btn>
                Nein
              </v-btn>
            </v-btn-toggle>
          </v-col>
          <v-col sm="4" md="2">
            <p>Kopfhörer</p>
            <v-btn-toggle v-model="tatoo" border
                          divided>
              <v-btn>
                Ja
              </v-btn>
              <v-btn>
                Nein
              </v-btn>
            </v-btn-toggle>
          </v-col>
          <v-col sm="12" md="8">
            <p>Anmerkungen</p>
            <v-text-field label="Kommentare"></v-text-field>
          </v-col>
        </v-row>

      </v-card-text>
      <v-card-actions class="d-flex flex-wrap">
        <v-btn
            text="Speichern & Schließen"
            color="green"
            @click="saveChanges"
        ></v-btn>
        <v-spacer></v-spacer>
        <v-btn
            text="Abbrechen"
            color="grey-darken-3"
            @click="closeModal"
        ></v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
import GogglesIcon from '../../assets/content/swimmer-characteristics/goggles-fill.svg';
import BikiniIcon from '../../assets/content/swimmer-characteristics/bikini.svg';
import SwimSuitIcon from '../../assets/content/swimmer-characteristics/one-piece-swimsuit-fill.svg';
import PantsIcon from '../../assets/content/swimmer-characteristics/pants-fill.svg';
import ShortPantsIcon from '../../assets/content/swimmer-characteristics/panty.svg';
import HairIcon from '../../assets/content/swimmer-characteristics/hair-fill.svg';
import HatIcon from '../../assets/content/swimmer-characteristics/24h-swimming-hat.svg';
import TattooIcon from '../../assets/content/swimmer-characteristics/tattoo-machine.svg';
import HeadphoneIcon from '../../assets/content/swimmer-characteristics/earpods-solid.svg';

export default {
  name: "EditSwimmerCharacteristicsModal",
  components: {
    GogglesIcon,
    BikiniIcon,
    ShortPantsIcon,
    HairIcon,
    PantsIcon,
    HatIcon,
    TattooIcon,
    HeadphoneIcon,
    SwimSuitIcon,
  },
  props: {},
  data() {
    return {
      swimSuit: null,
      swimsuitColour: null,
      googles: null,
      cap: null,
      hair: null,
      tatoo: null,


      showModal: false,
      swimmer: null,
      startSwimmingAfter: false,
      colorOptions: [
        'red-accent-4',
        'blue-accent-4',
        'green-darken-1',
        'purple-accent-4',
        'black',
        'white',
        'amber',
        'pink-accent-1',
      ],
      colorMap: [
        {
          id: "red",
          class: "red-accent-4",
          cssVar: "charsRed",
        },
        {
          id: "blue",
          class: "blue-accent-4",
          cssVar: "charsBlue",
        },
        {
          id: "green",
          class: "green-darken-1",
          cssVar: "charsGreen",
        },
        {
          id: "purple",
          class: "purple-accent-4",
          cssVar: "charsPurple",
        },
        {
          id: "black",
          class: "black",
          cssVar: "charsBlack",
        },
        {
          id: "white",
          class: "white",
          cssVar: "charsWhite",
        },
        {
          id: "yellow",
          class: "amber",
          cssVar: "charsYellow",
        },
        {
          id: "pink",
          class: "pink-accent-1",
          cssVar: "charsPink",
        },
      ],
      hairColorMap: [
        {
          id: "black",
          class: "black",
          cssVar: "charsBlack",
        },
        {
          id: "brown",
          class: "brown-darken-1",
          cssVar: "charsBrown",
        },
        {
          id: "orange",
          class: "orange-accent-4",
          cssVar: "charsOrange",
        },
        {
          id: "yellow",
          class: "amber",
          cssVar: "charsYellow",
        },
        {
          id: "grey",
          class: "grey-lighten-1",
          cssVar: "charsGrey",
        },
        {
          id: "white",
          class: "grey-lighten-4",
          cssVar: "charsLightGrey",
        },

        {
          id: "red",
          class: "red-accent-4",
          cssVar: "charsRed",
        },
        {
          id: "blue",
          class: "blue-accent-4",
          cssVar: "charsBlue",
        },
        {
          id: "green",
          class: "green-darken-1",
          cssVar: "charsGreen",
        },
      ]
    };
  },
  beforeDestroy() {
  },
  methods: {
    openModal(swimmer, startSwimmingAfter) {
      this.swimmer = swimmer;
      this.showModal = true;
      this.startSwimmingAfter = startSwimmingAfter;
    },
    closeModal() {
      this.showModal = false;
      this.$emit('close');
    },
    saveChanges() {
      this.$emit('saveChanges', this.swimmer);
      if (this.startSwimmingAfter) {
        this.$emit('startSwimming', this.swimmer);
      }
      this.closeModal();
    },
  },
  computed: {},
}
</script>
<style scoped>

.swimmer-characteristics-icon {
  width: 44px;
  height: 44px;
  background: var('--v-charsRed');
}

.characteristics-btn {
  width: 50px;
  height: 50px;
}

.icon-border-googles {
  stroke: black;
  stroke-width: 10px;
}
.icon-border-hat {
  stroke: black;
  stroke-width: 20px;
}
.icon-border-hair {
  stroke: black;
  stroke-width: 1px;

}

/* --- Neue Styles: Hervorhebung der ausgewählten Option --- */
/* Vuetify verwendet je nach Version unterschiedliche Active-Klassen. Wir decken beide ab. */
.v-btn--active,
.v-btn--is-active,
.characteristics-btn.v-btn--active,
.characteristics-btn.v-btn--is-active {
  transition: transform 120ms ease, box-shadow 120ms ease, border-color 120ms ease;
  box-shadow: 0 10px 20px rgba(0,0,200,0.18);
}

/* Leichter Hover-Effekt für alle options */
.v-btn:hover,
.characteristics-btn:hover {
  transform: translateY(-2px);
}

</style>