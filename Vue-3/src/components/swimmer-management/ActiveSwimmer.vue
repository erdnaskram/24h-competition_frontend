<template>
  <v-card
      class="active-swimmer"
      :class="{confirmClicked: isClickConfirmationActive}"
      style="position: relative;"
      @click="handleActiveSwimmerClicked"
      @touchstart.passive="onTouchStart"
      @touchend="onTouchEnd"
      @touchcancel="onTouchEnd"
  >
    <!-- ── Mobil ── -->
    <div class="d-md-none">
      <div class="d-flex align-center justify-space-between pa-3 pb-1">
        <a class="swimmer-name">
          {{ swimmer.id }} - {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }} ({{ swimmer.age }}J, <gender-icon :gender="swimmer.gender" :size="16" />)
        </a>
        <v-btn icon="mdi-account-edit" color="info" size="small" class="ml-2 flex-shrink-0" @click.stop="editActiveSwimmer" />
      </div>
      <div class="text-body-2 text-medium-emphasis px-3 pb-2">
        <v-icon size="14">mdi-swim</v-icon>
        {{ swimmer.swimDistance / 25 }} Bahnen / {{ formatDistance(swimmer.swimDistance) }}
      </div>
      <div class="characteristics-row px-3 pb-1">
        <template v-for="icon in characteristicIcons" :key="icon.key">
          <div class="swimmer-characteristics">
            <component :is="icon.component" class="swimmer-characteristics-icon" :style="icon.style" />
          </div>
        </template>
      </div>
      <div v-if="swimmer.characteristics.notes !== ''" class="swimmer-characteristics-notes mx-3 mb-3">
        {{ swimmer.characteristics.notes }}
      </div>
    </div>

    <!-- ── Tablet / PC ── -->
    <v-row class="d-none d-md-flex" no-gutters>
      <v-col md="6" class="pa-3 pb-2">
        <a class="swimmer-name">
          {{ swimmer.id }} - {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }} ({{ swimmer.age }}J, <gender-icon :gender="swimmer.gender" :size="16" />)
        </a>
        <div class="text-body-2 text-medium-emphasis mt-1">
          <v-icon size="14">mdi-swim</v-icon>
          {{ swimmer.swimDistance / 25 }} Bahnen / {{ formatDistance(swimmer.swimDistance) }}
        </div>
      </v-col>
      <v-col md="6" class="pa-3 pb-2 d-flex align-start">
        <div class="flex-grow-1 min-w-0">
          <div class="characteristics-row">
            <template v-for="icon in characteristicIcons" :key="icon.key">
              <div class="swimmer-characteristics">
                <component :is="icon.component" class="swimmer-characteristics-icon" :style="icon.style" />
              </div>
            </template>
          </div>
          <div v-if="swimmer.characteristics.notes !== ''" class="swimmer-characteristics-notes mt-1">
            {{ swimmer.characteristics.notes }}
          </div>
        </div>
        <v-btn icon="mdi-account-edit" color="info" size="large" class="ml-3 flex-shrink-0" @click.stop="editActiveSwimmer" />
      </v-col>
    </v-row>
    <!-- Medaillen-Animation -->
    <transition name="medal-pop">
      <div v-if="medalAnimation" class="medal-overlay" :style="{ borderColor: medalAnimation.color }">
        <v-icon size="56" :color="medalAnimation.color">mdi-medal</v-icon>
        <div class="medal-overlay-label" :style="{ color: medalAnimation.color }">{{ medalAnimation.label }}!</div>
      </div>
    </transition>

    <div class="px-3 pb-3">
      <v-progress-linear
          v-model="waitTimeProgress"
          :color="isCooldownActive ? 'primary' : 'green'"
          height="8"
          rounded
          :striped="isCooldownActive"
      ></v-progress-linear>
    </div>
  </v-card>
</template>
<script>
import GenderIcon from '../GenderIcon.vue';
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
    GenderIcon,
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
      minimizeSwimmer: false,
      longPressTimer: null,
      longPressTriggered: false,
      medalAnimation: null,
    };
  },
  computed: {
    characteristicIcons() {
      const c = this.swimmer.characteristics;
      const icons = [];
      const swimwearMap = { bikini: 'BikiniIcon', swimsuit: 'SwimSuitIcon', 'short-pants': 'ShortPantsIcon', trunks: 'PantsIcon' };
      if (swimwearMap[c.swimwearType]) icons.push({ key: 'swimwear', component: swimwearMap[c.swimwearType], style: { fill: c.swimwearColor } });
      if (c.googles !== 'none')  icons.push({ key: 'googles',  component: 'GogglesIcon',   style: { fill: c.googles } });
      if (c.hair !== 'none')     icons.push({ key: 'hair',     component: 'HairIcon',      style: { color: c.hair } });
      if (c.swimCap !== 'none')  icons.push({ key: 'swimCap',  component: 'HatIcon',       style: { fill: c.swimCap } });
      if (c.tattoo)              icons.push({ key: 'tattoo',   component: 'TattooIcon',    style: {} });
      if (c.headphones)          icons.push({ key: 'headphones', component: 'HeadphoneIcon', style: {} });
      return icons;
    },
  },
  methods: {
    formatDistance(distance) {
      return distance > 1000 ? `${(distance / 1000).toLocaleString('de-DE')} km` : `${distance.toLocaleString('de-DE')} m`;
    },
    editActiveSwimmer() {
      this.$emit('editActiveSwimmer', this.swimmer);
    },
    onTouchStart() {
      this.longPressTriggered = false;
      this.longPressTimer = setTimeout(() => {
        this.longPressTriggered = true;
        this.editActiveSwimmer();
      }, 500);
    },
    onTouchEnd() {
      clearTimeout(this.longPressTimer);
    },
    handleActiveSwimmerClicked() {
      if (this.longPressTriggered) return;
      if (this.lastIncrementTime && (new Date().getTime() - this.lastIncrementTime) < this.incrementCoolDown) {
        return;
      }
      const hasMedalUpgrade = this.incrementLaneCount();
      if (hasMedalUpgrade) {
        setTimeout(() => { this.$emit("activeSwimmerClicked", this.swimmer); }, 800);
      } else {
        this.$emit("activeSwimmerClicked", this.swimmer);
      }
    },
    checkMedalUpgrade(oldDist, newDist) {
      if (this.swimmer.age > 17) return false;
      const thresholds = [
        { at: 2001, label: 'Silber', color: '#9E9E9E' },
        { at: 5001, label: 'Gold',   color: '#F9A825' },
      ];
      for (const t of thresholds) {
        if (oldDist < t.at && newDist >= t.at) {
          this.medalAnimation = { label: t.label, color: t.color };
          setTimeout(() => { this.medalAnimation = null; }, 2500);
          return true;
        }
      }
      return false;
    },
    incrementLaneCount() {
      this.showClickConfirmation();
      const prevDistance = this.swimmer.swimDistance;
      this.swimmer.swimDistance += 50;
      const hasMedalUpgrade = this.checkMedalUpgrade(prevDistance, this.swimmer.swimDistance);
      this.lastIncrementTime = new Date().getTime();
      this.isCooldownActive = true;
      this.waitTimeProgress = 0;
      this.countDownCooldown();
      return hasMedalUpgrade;
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
  user-select: none;
  background-color: #f0f0f0;
  margin: 5px;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
}

.swimmer-name {
  font-size: 1.4rem;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
  display: block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.characteristics-row {
  display: flex;
  flex-wrap: nowrap;
  gap: 6px;
  overflow-x: auto;
  align-items: center;
}

.swimmer-characteristics {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  background-color: #dadada;
}

.swimmer-characteristics-notes {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 4px;
  background-color: #dadada;
  font-size: 0.85rem;
  line-height: 1.4;
}

.swimmer-characteristics-icon {
  width: 40px;
  height: 40px;
}

.medal-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.88);
  border-radius: 8px;
  border: 3px solid;
  z-index: 10;
  pointer-events: none;
  gap: 4px;
}

.medal-overlay-label {
  font-size: 1.6rem;
  font-weight: bold;
}

.medal-pop-enter-active {
  animation: medal-pop 2.5s ease-out forwards;
}

@keyframes medal-pop {
  0%   { opacity: 0; transform: scale(0.4); }
  15%  { opacity: 1; transform: scale(1.08); }
  30%  { transform: scale(1); }
  70%  { opacity: 1; }
  100% { opacity: 0; }
}

.confirmClicked {
  border-color: #4caf50; /* Vuetify-Grün */
}
</style>