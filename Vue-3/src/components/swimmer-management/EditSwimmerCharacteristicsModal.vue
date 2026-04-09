<template>
  <v-dialog v-model="showModal"
            max-width="1000"
            :fullscreen="isSmallScreen"
            persistent>
    <v-card title="Berabeiten" :class="['edit-characteristics-card', isSmallScreen ? 'd-flex flex-column' : '']" style="max-height: 100%;">
      <v-card-text :style="isSmallScreen ? 'overflow-y: auto; flex: 1 1 0; min-height: 0;' : 'overflow-y: auto;'">
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Badekleidung</p>
            <v-btn-toggle
                v-model="localSwimmer.characteristics.swimwearType"
                color="grey"
                border
                divided>
              <v-btn
                  value="bikini"
                  class="characteristics-btn">
                <bikini-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
              <v-btn
                  value="swimsuit"
                  class="characteristics-btn">
                <swim-suit-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
              <v-btn
                  value="short-pants"
                  class="characteristics-btn">
                <short-pants-icon
                    class="swimmer-characteristics-icon"/>
              </v-btn>
              <v-btn
                  value="trunks"
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
            <template v-if="isSmallScreen">
              <v-btn-toggle v-model="localSwimmer.characteristics.swimwearColor" border divided color="grey" class="mb-1">
                <v-btn class="characteristics-btn" v-for="color in colorMap.slice(0, half)" :key="color.id" :value="color.id">
                  <v-card :class="'bg-' + color.class" border="accent thin" style="width:40px;height:40px;border-radius:10%;"/>
                </v-btn>
              </v-btn-toggle>
              <v-btn-toggle v-model="localSwimmer.characteristics.swimwearColor" border divided color="grey">
                <v-btn class="characteristics-btn" v-for="color in colorMap.slice(half)" :key="color.id" :value="color.id">
                  <v-card :class="'bg-' + color.class" border="accent thin" style="width:40px;height:40px;border-radius:10%;"/>
                </v-btn>
              </v-btn-toggle>
            </template>
            <v-btn-toggle v-else v-model="localSwimmer.characteristics.swimwearColor" class="btn-toggle-wrap" border divided color="grey">
              <v-btn class="characteristics-btn" v-for="color in colorMap" :key="color.id" :value="color.id">
                <v-card :class="'bg-' + color.class" border="accent thin" style="width:40px;height:40px;border-radius:10%;"/>
              </v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Schwimmbrille</p>
            <template v-if="isSmallScreen">
              <v-btn-toggle v-model="localSwimmer.characteristics.googles" border divided color="grey" class="mb-1">
                <v-btn value="none"><goggles-icon class="swimmer-characteristics-icon char-purp" :style="{ fill: 'rgb(var(--v-theme-purple-accent-4))' }"/><v-icon style="position:absolute;top:35%;left:65%;transform:translate(-50%,-50%);" size="40" color="red">mdi-close-thick</v-icon></v-btn>
                <v-btn class="characteristics-btn" v-for="color in colorMap.slice(0, half)" :key="color.id" :value="color.id"><goggles-icon class="swimmer-characteristics-icon" :class="{'icon-border-googles': color.id==='white'}" :style="{ fill: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
              </v-btn-toggle>
              <v-btn-toggle v-model="localSwimmer.characteristics.googles" border divided color="grey">
                <v-btn class="characteristics-btn" v-for="color in colorMap.slice(half)" :key="color.id" :value="color.id"><goggles-icon class="swimmer-characteristics-icon" :class="{'icon-border-googles': color.id==='white'}" :style="{ fill: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
              </v-btn-toggle>
            </template>
            <v-btn-toggle v-else v-model="localSwimmer.characteristics.googles" class="btn-toggle-wrap" border divided color="grey">
              <v-btn value="none"><goggles-icon class="swimmer-characteristics-icon char-purp" :style="{ fill: 'rgb(var(--v-theme-purple-accent-4))' }"/><v-icon style="position:absolute;top:35%;left:65%;transform:translate(-50%,-50%);" size="40" color="red">mdi-close-thick</v-icon></v-btn>
              <v-btn class="characteristics-btn" v-for="color in colorMap" :key="color.id" :value="color.id"><goggles-icon class="swimmer-characteristics-icon" :class="{'icon-border-googles': color.id==='white'}" :style="{ fill: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Badekappe</p>
            <template v-if="isSmallScreen">
              <v-btn-toggle v-model="localSwimmer.characteristics.swimCap" border divided color="grey" class="mb-1">
                <v-btn value="none"><hat-icon class="swimmer-characteristics-icon"/><v-icon style="position:absolute;top:35%;left:65%;transform:translate(-50%,-50%);" size="40" color="red">mdi-close-thick</v-icon></v-btn>
                <v-btn class="characteristics-btn" v-for="color in colorMap.slice(0, half)" :key="color.id" :value="color.id"><hat-icon class="swimmer-characteristics-icon" :class="{'icon-border-hat': color.id==='white'}" :style="{ fill: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
              </v-btn-toggle>
              <v-btn-toggle v-model="localSwimmer.characteristics.swimCap" border divided color="grey">
                <v-btn class="characteristics-btn" v-for="color in colorMap.slice(half)" :key="color.id" :value="color.id"><hat-icon class="swimmer-characteristics-icon" :class="{'icon-border-hat': color.id==='white'}" :style="{ fill: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
              </v-btn-toggle>
            </template>
            <v-btn-toggle v-else v-model="localSwimmer.characteristics.swimCap" border divided color="grey">
              <v-btn value="none"><hat-icon class="swimmer-characteristics-icon"/><v-icon style="position:absolute;top:35%;left:65%;transform:translate(-50%,-50%);" size="40" color="red">mdi-close-thick</v-icon></v-btn>
              <v-btn class="characteristics-btn" v-for="color in colorMap" :key="color.id" :value="color.id"><hat-icon class="swimmer-characteristics-icon" :class="{'icon-border-hat': color.id==='white'}" :style="{ fill: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col
              class="py-2"
          >
            <p>Haarfarbe</p>
            <template v-if="isSmallScreen">
              <v-btn-toggle v-model="localSwimmer.characteristics.hair" border divided color="grey" class="mb-1">
                <v-btn value="none"><hair-icon class="swimmer-characteristics-icon"/><v-icon style="position:absolute;top:35%;left:65%;transform:translate(-50%,-50%);" size="40" color="red">mdi-close-thick</v-icon></v-btn>
                <v-btn class="characteristics-btn" v-for="color in hairColorMap.slice(0, hairHalf-1)" :key="color.id" :value="color.id"><hair-icon class="swimmer-characteristics-icon" :class="{'icon-border-hair': color.id==='white'}" :style="{ color: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
              </v-btn-toggle>
              <v-btn-toggle v-model="localSwimmer.characteristics.hair" border divided color="grey">
                <v-btn class="characteristics-btn" v-for="color in hairColorMap.slice(hairHalf-1)" :key="color.id" :value="color.id"><hair-icon class="swimmer-characteristics-icon" :class="{'icon-border-hair': color.id==='white'}" :style="{ color: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
              </v-btn-toggle>
            </template>
            <v-btn-toggle v-else v-model="localSwimmer.characteristics.hair" border divided color="grey">
              <v-btn value="none"><hair-icon class="swimmer-characteristics-icon"/><v-icon style="position:absolute;top:35%;left:65%;transform:translate(-50%,-50%);" size="40" color="red">mdi-close-thick</v-icon></v-btn>
              <v-btn class="characteristics-btn" v-for="color in hairColorMap" :key="color.id" :value="color.id"><hair-icon class="swimmer-characteristics-icon" :class="{'icon-border-hair': color.id==='white'}" :style="{ color: 'rgb(var(--v-theme-' + color.cssVar + '))' }"/></v-btn>
            </v-btn-toggle>
          </v-col>
        </v-row>
        <v-row>
          <v-col sm="4" md="2">
            <p>Tattoo</p>
            <v-btn-toggle
                v-model="localSwimmer.characteristics.tattoo"
                border
                divided
                color="grey">
              <v-btn :value="true">
                Ja
              </v-btn>
              <v-btn :value="false">
                Nein
              </v-btn>
            </v-btn-toggle>
          </v-col>
          <v-col sm="4" md="2">
            <p>Kopfhörer</p>
            <v-btn-toggle
                v-model="localSwimmer.characteristics.headphones"
                border
                divided
                color="grey">
              <v-btn :value="true">
                Ja
              </v-btn>
              <v-btn :value="false">
                Nein
              </v-btn>
            </v-btn-toggle>
          </v-col>
          <v-col sm="12" md="8">
            <p>Anmerkungen</p>
            <v-text-field v-model="localSwimmer.characteristics.notes" label="Kommentare"></v-text-field>
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
            style="color: white"
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
      headphones: null,


      windowWidth: window.innerWidth,
      showModal: false,
      localSwimmer: null,
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
  mounted() {
    this.windowWidth = window.innerWidth;
    this._onResize = () => { this.windowWidth = window.innerWidth; };
    window.addEventListener('resize', this._onResize);
  },
  beforeUnmount() {
    window.removeEventListener('resize', this._onResize);
  },
  methods: {
    openModal(swimmer, startSwimmingAfter) {
      this.localSwimmer = JSON.parse(JSON.stringify(swimmer));
      this.showModal = true;
      this.startSwimmingAfter = startSwimmingAfter;
    },
    closeModal() {
      this.showModal = false;
      this.$emit('close');
    },
    saveChanges() {
      this.$emit('saveChanges', this.localSwimmer);
      if (this.startSwimmingAfter) {
        this.$emit('startSwimming', this.localSwimmer);
      }
      this.closeModal();
    },
  },
  computed: {
    isSmallScreen() {
      return this.windowWidth < 960;
    },
    half() {
      return Math.ceil(this.colorMap.length / 2);
    },
    hairHalf() {
      return Math.ceil(this.hairColorMap.length / 2);
    },
  },
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
  box-shadow: 0 10px 20px rgba(0, 0, 200, 0.18);
}

/* Leichter Hover-Effekt für alle options */
.v-btn:hover,
.characteristics-btn:hover {
  transform: translateY(-2px);
}

/* Einreihige Darstellung auf PC/Tablet */
.btn-toggle-wrap {
  flex-wrap: nowrap !important;
  overflow-x: auto;
  max-width: 100%;
}
</style>
