<template>
  <!-- Begin page content -->
  <main role="main" class="container-fluid" style="margin-top: 60px">
    <b-row id="lanes" class="m-3">
      <b-col v-for="lane in laneStore.laneCount" :key="'chekin-lane-' + lane" class="laneInfo" cols="1"
             :class="getLaneColour(lane)"
             style="border: 1px solid black;">
        <h3>Bahn {{ lane }}</h3>
        <div style="overflow: hidden;">
          <div class="f-info">Anzahl <span class="count">5</span><br> Geschw <span class="speed">00:10</span></div>
        </div>
      </b-col>
    </b-row>
    <div class="row">
      <div class="col-sm-9">
        <div class="text-center">
          <button class="abc">A</button>
          <button class="abc">B</button>
          <button class="abc">C</button>
          <button class="abc">D</button>
          <button class="abc">E</button>
          <button class="abc">F</button>
          <button class="abc">G</button>
          <button class="abc">H</button>
          <button class="abc">I</button>
          <button class="abc">J</button>
          <button class="abc">K</button>
          <button class="abc">L</button>
          <button class="abc">M</button>
          <button class="abc">N</button>
          <button class="abc">O</button>
          <button class="abc">P</button>
          <button class="abc">Q</button>
          <button class="abc">R</button>
          <button class="abc">S</button>
          <button class="abc">T</button>
          <button class="abc">U</button>
          <button class="abc">V</button>
          <button class="abc">W</button>
          <button class="abc">X</button>
          <button class="abc">Y</button>
          <button class="abc">Z</button>
          <button class="abc">Ä</button>
          <button class="abc">Ö</button>
          <button class="abc">Ü</button>
        </div>
        <div id="activeSwimmers" class="container-fluid">
        </div>
      </div>
      <div class="col-sm-3">
        <b-row id="lanes" class="container-fluid">
          <b-col v-for="lane in laneStore.laneCount" :key="'chekin-lane-' + lane" class="laneInfo"
                 :class="getLaneColour(lane)"
                 style="border: 1px solid black;">
            <h1>Bahn {{ lane }}</h1>
            <div class="col" style="overflow: hidden; white-space: nowrap"><h2 class="f-name"></h2>
              <div class="f-info">Anzahl <span class="count">5</span>, Geschw <span class="speed">00:10</span></div>
            </div>
          </b-col>
        </b-row>
        <div style="margin-top: 15px">
          Farben: Gelb ab
          <b-form-input v-model="laneColourYellow" type="number" style="width: 70px; text-align: right"/>
          Rot
          ab
          <b-form-input v-model="laneColourRed" type="number" style="width: 70px; text-align: right"/>
        </div>

        <div id="totalDistance" style="margin-top: 15px; text-align: center; font-weight: bold; font-size: 40px">
          -- km
        </div>
      </div>
    </div>
  </main>

</template>

<script>
import {useLaneStore} from "../store/index.js";

export default {
  name: "CheckIn",
  setup() {
    const laneStore = useLaneStore();
    return {laneStore};
  },
  data() {
    return {
      laneColourYellow: 6,
      laneColourRed: 8,
    }
  },
  methods: {
    getLaneColour(lane) {
      if (lane < this.laneColourYellow) {
        return 'bg-success';
      } else if (lane < this.laneColourRed) {
        return 'bg-warning';
      } else {
        return 'bg-danger';
      }
    }
  },
}
</script>

<style scoped>
.abc {
  min-width: 40px;
  min-height: 40px;
  margin-bottom: 15px;
}

.activeSwimmer {
  transition: all 0.5s;
}

.activeSwimmer h2 {
  display: inline;
}

.activeSwimmer .f-info {
  display: inline;
}

.l-remarks {
  font-weight: bold;
}

#dialog {
  width: 800px;
}
</style>