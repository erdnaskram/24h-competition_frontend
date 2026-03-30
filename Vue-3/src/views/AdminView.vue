<template>
  <BRow class="m-2 pt-4">
    <BCol >
      <b-button variant="success" @click="openAddLaneModal()">Add Lane</b-button>
    </BCol>
  </BRow>
  <BRow class="p-2">
    <BCol>
      <div>
        <BRow v-for="(lane, index) in laneStore.lanes" :key="lane.id" class="m-2" style="background: #f2f2f2">
          <BCol  lg="2">
            <h2>{{ lane.id }} - {{ lane.name }}</h2>
          </BCol>
          <BCol lg="1">
            <BButton
                :disabled="!(index === laneStore.lanes.length - 1) || index === 0"
                variant="danger"
                pill
                @click="removeLane(lane.id)"
            >
              Löschen
            </BButton>
          </BCol>
        </BRow>
      </div>
    </BCol>
  </BRow>
  <BModal v-model="showAddLaneModal" title="Add New Lane" v-on:ok="addLane()">
    <div>
      <label for="laneId">Lane ID:</label>
      <BFormInput type="text" v-model="newId" id="laneId" disabled/>
    </div>
    <div>
      <label for="laneName">Lane Name:</label>
      <BFormInput type="text" v-model="newLaneName" id="laneName"/>
    </div>
  </BModal>
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
      localLaneCount: 0,
      showAddLaneModal: false,
      newLaneName: '',
    };
  },
  mounted() {
    this.localLaneCount = this.laneStore.laneCount;
  },
  computed: {
    newId() {
      return this.laneStore.lanes.length ? Math.max(...this.laneStore.lanes.map(lane => lane.id)) + 1 : 1;
    }
  },
  methods: {
    goToAbout() {
      this.$router.push('/about')
    },
    updateLaneCount(newCount) {
      this.laneStore.setLaneCount(newCount);
    },
    removeLane(id) {
      this.laneStore.removeLane(id);
    },
    addLane() {
      const newLane = {
        id: this.newId,
        name: this.newLaneName
      };
      console.log(newLane);
      this.laneStore.addLane(newLane);
      this.showAddLaneModal = false;
      this.newLaneName = '';
    },
    openAddLaneModal() {
      this.showAddLaneModal = true;
      this.newLaneName = 'Bahn ' + this.newId;
    }
  },
  watch: {
    laneStore: {
      handler() {
        this.localLaneCount = this.laneStore.laneCount;
      },
      deep: true
    },
  }
}
</script>

<style scoped>

</style>