<template>
  <h1>Bitte Bahn wählen</h1>
  <b-row class="justify-content-md-center">
    <b-col class="col-6 col-md-8 col-lg-6">
      <b-button-group class="mb-2" :vertical="isButtonVertical">
        <b-button
            v-for="lane in lanes" :id="'laneButton' + lane.id"
            @click="this.$router.push('/lane/' + lane.id)"
            variant="primary"
            size="lg">
          {{ 'Bahn ' + lane.id }}
        </b-button>
      </b-button-group>
    </b-col>
    <b-col class="col-6 col-md-4 col-lg-6 justify-content-md-center">
      <b-button
          @click="this.$router.push('/results')"
          variant="success"
          size="lg">Ergebnisse</b-button>
    </b-col>
  </b-row>

  <div>
    <b-button v-b-modal.modal-1>Launch demo modal</b-button>

    <b-modal id="modal-1" title="BootstrapVue">
      <p class="my-4">Hello from modal!</p>
    </b-modal>
  </div>

</template>

<script>
export default {
  name: "LaneSelect",
  data() {
    return {
      lanes: [
        {id: 1, name: "Bahn 1"},
        {id: 2, name: "Bahn 2"},
        {id: 3, name: "Bahn 3"},
        {id: 4, name: "Bahn 4"},
        {id: 5, name: "Bahn 5"},
      ],
      width: window.innerWidth,
    };
  },
  computed: {
    isButtonVertical() {
      return this.width < 768;
    }
  },
  created() {
    window.addEventListener('resize', this.updateWidth);
  },
  destroyed() {
    window.removeEventListener('resize', this.updateWidth);
  },
  methods: {
    updateWidth() {
      this.width = window.innerWidth;
    }
  }
}
</script>

<style scoped>
</style>