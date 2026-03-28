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
            ({{ swimmer.age }})
          </a>
        </v-col>
      </v-row>
      <v-row class="text-left">
        <v-col class="swimmer-info">
          <v-chip color="primary">
            {{ formatDistance(swimmer.swimDistance) }}
          </v-chip>
        </v-col>
        <v-col  class="swimmer-info">
          <v-chip color="primary">
            {{ formatLaneCount(swimmer.swimDistance) }}
          </v-chip>
        </v-col>
        <v-col
            v-if="swimmer.team !== ''"
            class="swimmer-info">
          <v-chip color="primary">
            {{ swimmer.team }}
            <v-icon class="ml-2" icon="mdi-account-group"></v-icon>
          </v-chip>
        </v-col>
        <v-col
            v-else-if="swimmer.family !== ''"
            class="swimmer-info">
          <v-chip color="primary">
            {{ swimmer.family }}
            <v-icon class="ml-2" >mdi-home</v-icon>
          </v-chip>
        </v-col>
      </v-row>
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
export default {
  name: "EditActiveSwimmerModal",
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
    formatLaneCount(distance) {
      return `${distance / 25} Bahnen`
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

.swimmer-info {
  padding-left: 4px;
  padding-right: 4px;
}
</style>