<template>
  <v-container class="py-6">

    <v-row align="center" class="mb-4">
      <v-col>
        <span class="text-h5 font-weight-bold">Bahn-Verwaltung</span>
      </v-col>
      <v-col cols="auto">
        <v-btn
            color="success"
            prepend-icon="mdi-plus"
            @click="openAddLaneModal"
        >
          Bahn hinzufügen
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col
          v-for="(lane, index) in laneStore.lanes"
          :key="lane.id"
          cols="12"
      >
        <v-card variant="flat" style="background-color: #f2f2f2;" class="px-4 py-3">
          <v-row align="center" no-gutters>
            <v-col>
              <span class="text-h6">{{ lane.id }} – {{ lane.name }}</span>
            </v-col>
            <v-col cols="auto">
              <v-btn
                  color="error"
                  variant="tonal"
                  prepend-icon="mdi-delete"
                  :disabled="!(index === laneStore.lanes.length - 1) || index === 0"
                  @click="removeLane(lane.id)"
              >
                Löschen
              </v-btn>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="showAddLaneModal" max-width="480" persistent>
      <v-card title="Neue Bahn hinzufügen">
        <v-card-text>
          <v-text-field
              :model-value="newId"
              label="Bahn-ID"
              disabled
              variant="outlined"
              class="mb-2"
          />
          <v-text-field
              v-model="newLaneName"
              label="Bahn-Name"
              variant="outlined"
              autofocus
              @keyup.enter="addLane"
          />
        </v-card-text>
        <v-card-actions>
          <v-btn color="success" @click="addLane">Hinzufügen</v-btn>
          <v-spacer />
          <v-btn color="grey-darken-2" @click="showAddLaneModal = false">Abbrechen</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script>
import {useLaneStore} from "../store/index.js";

export default {
  setup() {
    const laneStore = useLaneStore();
    return {laneStore};
  },
  data() {
    return {
      showAddLaneModal: false,
      newLaneName: '',
    };
  },
  computed: {
    newId() {
      return this.laneStore.lanes.length
          ? Math.max(...this.laneStore.lanes.map(lane => lane.id)) + 1
          : 1;
    }
  },
  methods: {
    removeLane(id) {
      this.laneStore.removeLane(id);
    },
    addLane() {
      this.laneStore.addLane({ id: this.newId, name: this.newLaneName });
      this.showAddLaneModal = false;
      this.newLaneName = '';
    },
    openAddLaneModal() {
      this.newLaneName = 'Bahn ' + this.newId;
      this.showAddLaneModal = true;
    },
  },
}
</script>
