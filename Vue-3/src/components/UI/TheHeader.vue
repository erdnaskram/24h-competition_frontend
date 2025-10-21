<template>
  <BNavbar v-b-color-mode="'dark'" class="dark-navbar" fixed="top">
    <BNavbarBrand to="/">DLRG 24 Hours</BNavbarBrand>
    <BNavbarNav class="ms-auto mb-2 mb-lg-0">
      <BNavItemDropdown text="Bahnen" right>
        <BDropdownItem to="/lane">Bahn Auswahl</BDropdownItem>
        <BDropdownItem v-for="lane in laneStore.laneCount" :key="'dropDownItemLane' + lane" :to="'/lane/' + lane">Bahn {{lane}}</BDropdownItem>
      </BNavItemDropdown>
      <BNavItem to="/results">Ergebnisse</BNavItem>
      <BNavItem to="/checkin">CheckIn</BNavItem>
    </BNavbarNav>
  </BNavbar>
</template>

<script>
import {useLaneStore} from "../../store/index.js";

export default {
  name: "TheHeader",
  setup() {
    const laneStore = useLaneStore();
    return {laneStore};
  },
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