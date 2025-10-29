<template>
  <v-card
      class="active-swimmer"
      :class="{confirmClicked: isClickConfirmationActive}"
      @click="incrementLaneCount"
  >
    <v-row>
      <v-col class="col-12 col-md-6">
        <v-row class="p-3 pb-2">
          <a class="swimmer-name">
            {{ swimmer.id }} -
            {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }}
            ({{ swimmer.age }}J)
          </a>
        </v-row>
        <v-row class="text-left p-3 pt-0 d-none d-md-flex">
          <v-col class="swimmer-info">
            <v-chip :color="swimmer.swimDistance > 1000 ? 'primary' : 'info'"
                    :class="{ 'font-italic': swimmer.swimDistance <= 1000 }"
                    size="large">
              {{ formatDistance(swimmer.swimDistance) }}
            </v-chip>
          </v-col>
          <v-col class="swimmer-info">
            <v-chip color="primary" size="large">
              {{ formatLaneCount(swimmer.swimDistance) }}
            </v-chip>
          </v-col>
          <v-col v-if="swimmer.team !== ''" class="swimmer-info">
            <v-chip color="primary" size="large">
              {{ swimmer.team }}
              <v-icon class="ml-2" icon="mdi-account-group"></v-icon>
            </v-chip>
          </v-col>
          <v-col v-else-if="swimmer.family !== ''" class="swimmer-info">
            <v-chip color="primary" size="large">
              {{ swimmer.family }}
              <v-icon class="ml-2">mdi-home</v-icon>
            </v-chip>
          </v-col>
        </v-row>
      </v-col>
      <v-col class="col-12 col-md-6 d-flex justify-center"
             style="height: auto;">
        <div
            style="
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      justify-content: flex-start;
    "
        >
          <div
              v-if="swimmer.characteristics.swimwearType === 'bikini'"
              class="swimmer-characteristics">
            <bikini-icon
                class="swimmer-characteristics-icon"
                :style="{ fill: swimmer.characteristics.swimwearColor }"
            />
          </div>
          <div
              v-if="swimmer.characteristics.swimwearType === 'swimsuit'"
              class="swimmer-characteristics">
            <swim-suit-icon
                class="swimmer-characteristics-icon"
                :style="{ fill: swimmer.characteristics.swimwearColor }"
            />
          </div>
          <div
              v-else-if="swimmer.characteristics.swimwearType === 'short-pants'"
              class="swimmer-characteristics">
            <short-pants-icon
                class="swimmer-characteristics-icon"
                :style="{ fill: swimmer.characteristics.swimwearColor}"
            ></short-pants-icon>
          </div>
          <div
              v-else-if="swimmer.characteristics.swimwearType === 'trunks'"
              class="swimmer-characteristics">
            <pants-icon
                class="swimmer-characteristics-icon"
                :style="{ fill: swimmer.characteristics.swimwearColor}"
            ></pants-icon>
          </div>
          <div
              v-if="swimmer.characteristics.googles !== 'none'"
              class="swimmer-characteristics">
            <goggles-icon
                class="swimmer-characteristics-icon"
                :style="{ fill: swimmer.characteristics.googles}"
            ></goggles-icon>
          </div>
          <div class="swimmer-characteristics">
            <hair-icon
                class="swimmer-characteristics-icon"
                :style="{ color: swimmer.characteristics.hair }"
            ></hair-icon>
          </div>
          <div
              v-if="swimmer.characteristics.swimCap !== 'none'"
              class="swimmer-characteristics">
            <hat-icon
                class="swimmer-characteristics-icon"
                :style="{ fill: swimmer.characteristics.swimCap }"
            ></hat-icon>
          </div>
          <div
              v-if="swimmer.characteristics.tattoo"
              class="swimmer-characteristics">
            <tattoo-icon
                class="swimmer-characteristics-icon"
            ></tattoo-icon>
          </div>
          <div
              v-if="swimmer.characteristics.headphones"
              class="swimmer-characteristics">
            <headphone-icon
                class="swimmer-characteristics-icon"
            ></headphone-icon>
          </div>


          <div
              v-if="swimmer.characteristics.notes !== ''"
              class="swimmer-characteristics-notes">
            <a>{{ swimmer.characteristics.notes }}</a>
          </div>
        </div>
        <v-spacer></v-spacer>
        <v-btn
            icon="mdi-account-edit"
            color="info"
            size="large"
            @click.stop="editActiveSwimmer"
        ></v-btn>
      </v-col>
    </v-row>
    <v-progress-linear
        v-model="waitTimeProgress"
        :color="isCooldownActive ? 'primary' : 'green'"
        height="8"
        rounded
        :striped="isCooldownActive"
    ></v-progress-linear>
  </v-card>
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
  name: "activeSwimmer",
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
  props: {
    swimmer: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isInScope: false,
      lastIncrementTime: null,
      incrementCoolDown: 10_000, // 10 seconds
      waitTimeProgress: 0,
      isCooldownActive: false,
      now: Date.now(),
      isClickConfirmationActive: false,
    };
  },
  computed: {
  },
  methods: {
    formatDistance(distance) {
      return distance > 1000 ? `${(distance / 1000).toLocaleString('de-DE')} km` : `${distance.toLocaleString('de-DE')} m`;
    },
    formatLaneCount(distance) {
      return `${distance / 25} Bahnen`
    },
    editActiveSwimmer() {
      this.$emit('editActiveSwimmer', this.swimmer);
    },
    incrementLaneCount() {
      const currentTime = new Date().getTime();
      if (this.lastIncrementTime && (currentTime - this.lastIncrementTime) < this.incrementCoolDown) {
        return; // Cooldown period not yet passed
      }
      this.showClickConfirmation();
      this.swimmer.swimDistance += 50;
      this.lastIncrementTime = currentTime;
      this.isCooldownActive = true;
      this.waitTimeProgress = 0;
      this.countDownCooldown()
    },
    countDownCooldown() {
      setTimeout(() => {
        this.waitTimeProgress += 10;
        if (this.waitTimeProgress < 100) {
          this.countDownCooldown();
        } else {
          this.isCooldownActive = false;
        }
      }, this.incrementCoolDown / 10);
    },
    showClickConfirmation() {
      this.isClickConfirmationActive = true;
      setTimeout(() => {
        this.isClickConfirmationActive = false;
      }, 500);
    }
  },
}
</script>

<style scoped>
.active-swimmer {
  background-color: #f0f0f0;
  border: 3px solid gray;
  padding: 10px;
  margin: 5px;
  border-radius: 8px;
  cursor: pointer;
}

.swimmer-name {
  font-size: 1.7em;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
}

.swimmer-info {
  padding-left: 4px;
  padding-right: 4px;
}

.font-italic {
  font-style: italic;
}

.swimmer-characteristics {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  background-color: #dadada;
}

.swimmer-characteristics-notes {
  width: 255px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  background-color: #dadada;
}

.swimmer-characteristics-icon {
  width: 40px;
  height: 40px;
}

.confirmClicked {
  border-color: #4caf50; /* Vuetify-Grün */
}
</style>