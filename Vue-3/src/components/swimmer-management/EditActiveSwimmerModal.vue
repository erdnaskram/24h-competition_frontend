<template>
  <v-dialog v-model="showModal"
    max-width="600"
    persistent >
    <v-card>
      <v-card-text>
      <v-row>
        <v-col>
          <a class="swimmer-name">
            {{ swimmer.id }} -
            {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }}
            ({{ swimmer.age }}J, <gender-icon :gender="swimmer.gender" :size="18" />)
          </a>
        </v-col>
      </v-row>
      <div class="text-body-1 font-weight-bold mt-1" style="color: #013157;">
        <v-icon size="18">mdi-swim</v-icon>
        {{ swimmer.swimDistance / 25 }} Bahnen / {{ formatDistance(swimmer.swimDistance) }}
      </div>
      <div v-if="swimmer.age <= 17 && swimmer.swimDistance >= 1" class="d-flex align-center gap-2 mt-2 mb-2">
        <v-icon size="20" :color="getMedal(swimmer.swimDistance).color">mdi-medal</v-icon>
        <span class="text-body-1 font-weight-bold" :style="{ color: getMedal(swimmer.swimDistance).color }">
          {{ getMedal(swimmer.swimDistance).label }}
        </span>
        <span class="text-body-2 text-medium-emphasis ml-3">{{ getMedal(swimmer.swimDistance).range }}</span>
      </div>
        <v-row>
          <v-col>
            <v-btn
                @click="editSwimmer">
              Schwimmer Merkmale bearbeiten
              <v-icon class="ml-2">mdi-account-edit</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions class="d-flex flex-wrap">
      <v-btn
          text="Pause"
          color="blue"
          @click="breakSwimming"
      ></v-btn>
      <v-btn
          text="Ausblenden"
          color="grey"
          @click="minimizeSwimmer"
      ></v-btn>
      <v-btn
          text="Bahn verlassen"
          color="deep-orange"
          @click="leaveLane"
      ></v-btn>
        <v-spacer></v-spacer>
      <v-btn
          text="Schließen"
          color="grey-darken-3"
          @click="closeModal"
      ></v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import GenderIcon from '../GenderIcon.vue';
export default {
  name: "EditActiveSwimmerModal",
  components: { GenderIcon },
  props: {
  },
  data() {
    return {
      showModal: false,
      swimmer: null,
    };
  },
  methods: {
    openModal(swimmer) {
      this.swimmer = swimmer;
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
      this.$emit('close');
    },
    formatDistance(distance) {
      return distance > 1000 ? `${(distance / 1000).toLocaleString('de-DE')} km` : `${distance.toLocaleString('de-DE')} m`;
    },
    getMedal(distance) {
      if (distance >= 5001) return { label: 'Gold',   color: '#F9A825', range: 'ab 5.001 m' };
      if (distance >= 2001) return { label: 'Silber', color: '#9E9E9E', range: '2.001 – 5.000 m' };
      return                       { label: 'Bronze', color: '#A1632A', range: '1 – 2.000 m' };
    },
    breakSwimming() {
      this.$emit('breakSwimming', this.swimmer);
      this.closeModal();
    },
    leaveLane() {
      this.$emit('leaveLane', this.swimmer);
      this.closeModal();
    },
    editSwimmer() {
      this.$emit('editSwimmer', this.swimmer);
      this.closeModal();
    },
    minimizeSwimmer() {
      this.$emit('minimizeSwimmer', this.swimmer);
      this.closeModal();
    },
  },
  computed: {},
}
</script>

<style scoped>
.swimmer-name {
  font-size: 1.7em;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
}
</style>