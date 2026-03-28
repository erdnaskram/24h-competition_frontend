<template>
  <b-navbar class="dark-navbar" v-b-color-mode="'dark'" fixed="top">
    <BNavbarBrand to="/">DLRG 24 Hours</BNavbarBrand>
    <BNavbarBrand> - Bahn {{ laneId }}</BNavbarBrand>
    <BNavbarNav class="ms-auto mb-2 mb-lg-0">
      <BNavItem
          v-for="lane in laneStore.lanes"
          :key="'dropDownItemLane' + lane.id"
          :variant="lane.id === laneId ? 'success' : ''"
          :disabled="laneId === lane.id"
          :to="'/lane/' + lane.id"
      >
        {{ lane.name }}
      </BNavItem>

    </BNavbarNav>
    <BNavbarNav>
      <BNavForm>
        <counter-message-modal></counter-message-modal>
      </BNavForm>
    </BNavbarNav>
  </b-navbar>

  <section class="mt-5">

    <!-- Begin page content -->
    <main role="main" class="container-fluid" style="margin-top: 60px">
      <v-row>
        <v-col class="col-12 col-lg-8">
          <active-swimmer
              v-for="swimmer in activeSwimmers"
              :key="'activeSwimmer' + swimmer.id"
              :swimmer="swimmer"
              @editActiveSwimmer="editActiveSwimmer"
              @activeSwimmerClicked="rearrangeSwimmer"
          ></active-swimmer>
        </v-col>
        <v-col class="col-12 col-lg-4">
          <div id="inactiveSwimmers">
            <inactive-swimmer
                v-for="swimmer in inactiveSwimmers"
                :swimmer="swimmer"
                :hide-info-default="true"
                @inactiveSwimmerClicked="inactiveSwimmerClicked"
            ></inactive-swimmer>
          </div>
        </v-col>
      </v-row>
    </main>

    <edit-inactive-swimmer-modal
        ref="displayInactiveSwimmerModal"
        @startSwimming="addSwimmerToActiveList"
        @leaveLane="removeSwimmerFromLane"
        @editSwimmer="editSwimmerCharacteristics"
        @close="">
    </edit-inactive-swimmer-modal>
    <edit-active-swimmer-modal
        ref="editActiveSwimmerModal"
        @breakSwimming="removeSwimmerFromActiveList"
        @leaveLane="removeSwimmerFromLane"
        @editSwimmer="editSwimmerCharacteristics"
        @close="">
    </edit-active-swimmer-modal>
    <edit-swimmer-characteristics-modal
        ref="editSwimmerCharacteristicsModal"
        @saveChanges="saveSwimmerCharacteristicsChanges"
        @startSwimming="addSwimmerToActiveList"
        @close="">
    </edit-swimmer-characteristics-modal>
  </section>
</template>

<script>
import {useLaneStore} from '../store';
import CounterMessageModal from "../components/lane-management/CounterMessageModal.vue";
import InactiveSwimmer from "../components/swimmer-management/InactiveSwimmer.vue";
import EditInactiveSwimmerModal from "../components/swimmer-management/EditInactiveSwimmerModal.vue";
import ActiveSwimmer from "../components/swimmer-management/ActiveSwimmer.vue";
import EditActiveSwimmerModal from "../components/swimmer-management/EditActiveSwimmerModal.vue";
import EditSwimmerCharacteristicsModal from "../components/swimmer-management/EditSwimmerCharacteristicsModal.vue";

export default {
  name: "LaneSelect",
  components: {
    EditSwimmerCharacteristicsModal,
    EditInactiveSwimmerModal,
    EditActiveSwimmerModal,
    ActiveSwimmer,
    InactiveSwimmer,
    CounterMessageModal
  },
  data() {
    return {
      laneId: null,
      lanes: [
        {id: 1, name: "Bahn 1"},
        {id: 2, name: "Bahn 2"},
        {id: 3, name: "Bahn 3"},
        {id: 4, name: "Bahn 4"},
        {id: 5, name: "Bahn 5"},
      ],
      showEditSwimmerModal: false,
      showMessageModal: false,
      swimmers: [
        {
          id: 1,
          isActive: false,
          swimmerName: {
            first: "Max",
            last: "Mustermann"
          },
          age: 25,
          swimDistance: 1050,
          team: "Team A",
          family: "",
          gender: "male",
          characteristics: {
            swimwearType: "trunks",
            swimwearColor: "green",
            googles: "blue",
            swimCap: "red",
            hair: "brown",
            tattoo: false,
            headphones: false,
            notes: ""
          }
        },
        {
          id: 2,
          isActive: true,
          swimmerName: {first: "Erika", last: "Musterfrau"},
          age: 30,
          swimDistance: 1500,
          team: "",
          family: "Millers and Friends",
          gender: "female",
          characteristics: {
            swimwearType: "bikini",
            swimwearColor: "red",
            googles: "black",
            swimCap: "black",
            hair: "yellow",
            tattoo: false,
            headphones: true,
            notes: "Prefers to swim in the evening."
          }
        },
        {
          id: 3,
          isActive: false,
          swimmerName: {
            first: "Hans",
            last: "Müller"
          },
          age: 28,
          swimDistance: 20350,
          team: "Team B",
          family: "",
          gender: "male",
          characteristics: {
            swimwearType: "trunks",
            swimwearColor: "red",
            googles: "black",
            swimCap: "none",
            hair: "black",
            tattoo: true,
            headphones: true,
            notes: "Has a tattoo on his left arm."
          }
        },
        {
          id: 4,
          isActive: false,
          swimmerName: {
            first: "Anna",
            last: "Schmidt"
          },
          age: 22,
          swimDistance: 5000,
          team: "Team A",
          family: "",
          gender: "female",
          characteristics: {
            swimwearType: "swimsuit",
            swimwearColor: "blue",
            googles: "none",
            swimCap: "yellow",
            hair: "green",
            tattoo: false,
            headphones: false,
            notes: "Prefers to swim in the morning."
          }
        },
        {
          id: 5,
          isActive: false,
          swimmerName: {
            first: "Peter",
            last: "Schneider"
          },
          age: 35,
          swimDistance: 10000,
          team: "",
          family: "",
          gender: "male",
          characteristics: {
            swimwearType: "short-pants",
            swimwearColor: "blue",
            googles: "green",
            swimCap: "white",
            hair: "grey",
            tattoo: false,
            headphones: true,
            notes: "Enjoys listening to music while swimming."
          }
        },
        {
          id: 6,
          isActive: false,
          swimmerName: {
            first: "Julia",
            last: "Fischer"
          },
          age: 8,
          swimDistance: 150,
          team: "",
          family: "Millers",
          gender: "female",
          characteristics: {
            swimwearType: "bikini",
            swimwearColor: "pink",
            googles: "purple",
            swimCap: "pink",
            hair: "yellow",
            tattoo: false,
            headphones: false,
            notes: "Loves swimming with her friends."
          }
        },
      ],
      selectedInactiveSwimmer: null,
    };
  },
  setup() {
    const laneStore = useLaneStore();
    return {
      laneStore
    };
  },
  mounted() {
    this.laneId = Number.parseInt(this.$route.params.id);
  },
  beforeUpdate() {
    this.laneId = Number.parseInt(this.$route.params.id);
  },
  computed: {
    activeSwimmers() {
      return this.swimmers.filter(s => s.isActive)
    },
    inactiveSwimmers() {
      return this.swimmers.filter(s => !s.isActive)
    }
  },
  methods: {
    inactiveSwimmerClicked(swimmer) {
      this.selectedInactiveSwimmer = swimmer;
      this.$refs.displayInactiveSwimmerModal.openModal(swimmer);
    },
    addSwimmerToActiveList(swimmer) {
      swimmer.isActive = true;
    },
    removeSwimmerFromLane(swimmer) {
      this.swimmers = this.swimmers.filter(s => s.id !== swimmer.id);
    },
    editActiveSwimmer(swimmer) {
      this.$refs.editActiveSwimmerModal.openModal(swimmer);
    },
    removeSwimmerFromActiveList(swimmer) {
      swimmer.isActive = false;
    },
    editSwimmerCharacteristics(swimmer, startSwimming = false) {
      this.$refs.editSwimmerCharacteristicsModal.openModal(swimmer, startSwimming);
    },
    saveSwimmerCharacteristicsChanges(updatedSwimmer) {
      const index = this.swimmers.findIndex(s => s.id === updatedSwimmer.id);
      if (index !== -1) {
        this.swimmers.splice(index, 1, updatedSwimmer);
      }
    },
    rearrangeSwimmer(swimmer) {
      const index = this.swimmers.findIndex(s => s.id === swimmer.id);
      if (index !== -1) {
        this.swimmers.splice(index, 1);
        this.swimmers.push(swimmer);
      }
    },
  }
}
</script>

<style scoped>
.dark-navbar {
  background-color: #013157 !important;
  color: white !important;
  height: 80px; /* Adjust the height as needed */
  padding: 20px; /* Adjust the padding as needed */
  font-size: 1.2rem; /* Adjust the font size as needed */
}

</style>