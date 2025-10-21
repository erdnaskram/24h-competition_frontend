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
      <BRow>
        <BCol class="col-sm-8">
          <div id="activeSwimmers" class="container-fluid">
            <BRow>
              <BCol>
                <BButton v-b-toggle.collapse-1 variant="primary">Bahn 1</BButton>
                <BCollapse id="collapse-1" class="mt-2">
                  <b-card>
                    <b-card-text>
                      <div class="row active swimmer" style="border: 1px solid black;">
                        <div class="col" style="overflow: hidden; white-space: nowrap">
                          <div class="f-edit"
                               style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
                          <div class="f-attributes"></div>
                          <h2 class="f-name"></h2>
                          <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                              class="distance">-- Bahnen / -- m</span></div>
                        </div>
                      </div>
                    </b-card-text>
                  </b-card>
                </BCollapse>
              </BCol>
            </BRow>

            <div class="row activeSwimmer" style="border: 1px solid black;">
              <div class="col" style="overflow: hidden; white-space: nowrap">
                <div class="f-edit"
                     style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
                <div class="f-attributes"></div>
                <h2 class="f-name"></h2>
                <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                    class="distance">-- Bahnen / -- m</span></div>
              </div>
            </div>
            <div class="row activeSwimmer" style="border: 1px solid black;">
              <div class="col" style="overflow: hidden; white-space: nowrap">
                <div class="f-edit"
                     style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
                <div class="f-attributes">
                  <div
                      style="float: right; width: 50px; height: 50px; margin: 2px; background-image: url(../components/content/Tattoo.png);"></div>
                </div>
                <h2 class="f-name"></h2>
                <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                    class="distance">-- Bahnen / -- m</span></div>
              </div>
            </div>
          </div>
        </BCol>
        <BCol class="col-sm-4">
          <div id="inactiveSwimmers">
            <inactive-swimmer
                v-for="swimmer in swimmers"
                :swimmer="swimmer"
                :hide-info-default="true"
            ></inactive-swimmer>
            <div class="row activeSwimmer" style="border: 1px solid black;">
              <div class="col" style="overflow: hidden; white-space: nowrap">
                <div class="f-edit"
                     style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
                <div class="f-attributes"></div>
                <h2 class="f-name"></h2>
                <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                    class="distance">-- Bahnen / -- m</span></div>
              </div>
            </div>
            '
            <div class="row activeSwimmer" style="border: 1px solid black;">
              <div class="col" style="overflow: hidden; white-space: nowrap">
                <div class="f-edit"
                     style="float: right; width: 75px; height: 50px; margin: 5px; padding-left: 20px; background-image: url(../components/content/pen_blue.png); background-repeat: no-repeat; background-position: center; background-color: red;"></div>
                <div class="f-attributes"></div>
                <h2 class="f-name"></h2>
                <div class="f-info">Nächster Anschlag: <span class="countdown">--:--</span>, Distanz: <span
                    class="distance">-- Bahnen / -- m</span></div>
              </div>
            </div>
            '
          </div>
        </BCol>
      </BRow>
    </main>


    <edit-swimmer-modal></edit-swimmer-modal>
  </section>
</template>

<script>
import {useLaneStore} from '../store';
import EditSwimmerModal from "../components/lane-management/EditSwimmerModal.vue";
import CounterMessageModal from "../components/lane-management/CounterMessageModal.vue";
import InactiveSwimmer from "../components/swimmer-management/InactiveSwimmer.vue";

export default {
  name: "LaneSelect",
  components: {InactiveSwimmer, CounterMessageModal, EditSwimmerModal},
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
          swimmerName: {first: "Max", last: "Mustermann"},
          age: 25,
          swimDistance: 1050,
          team: "Team A",
          family: ""
        },
        {
          id: 2,
          swimmerName: {first: "Erika", last: "Musterfrau"},
          age: 30,
          swimDistance: 1500,
          team: "",
          family: "Millers"
        },
        {id: 3, swimmerName: {first: "Hans", last: "Müller"}, age: 28, swimDistance: 20350, team: "Team B", family: ""},
        {id: 4, swimmerName: {first: "Anna", last: "Schmidt"}, age: 22, swimDistance: 5000, team: "Team A", family: ""},
        {id: 5, swimmerName: {first: "Peter", last: "Schneider"}, age: 35, swimDistance: 10000, team: "", family: ""},
        {id: 6, swimmerName: {first: "Julia", last: "Fischer"}, age: 8, swimDistance: 150, team: "", family: "Millers"},
      ],
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