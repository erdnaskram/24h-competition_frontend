<template>
  <v-card
      class="active-swimmer"
  >
    <v-row align="center">
      <v-col class="col-12 col-md-10">
        <a class="swimmer-name">
          {{ swimmer.id }} -
          {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }}
          ({{ swimmer.age }}J)
        </a>
      </v-col>
      <v-col class="col-12 col-md-2 d-flex justify-end align-center">
        <v-btn
            icon="mdi-eye-off"
            color="blue"
            size="small"
            @click.stop="showActiveSwimmer"
            :title="'Ausblenden'"
        ></v-btn>
      </v-col>
    </v-row>
  </v-card>
</template>
<script>

export default {
  name: "minimizedSwimmer",
  components: {
  },
  props: {
    swimmer: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
    };
  },
  computed: {},
  methods: {
    showActiveSwimmer() {
      this.$emit('showActiveSwimmer', this.swimmer);
    },
  },
}
</script>

<style scoped>
.active-swimmer {
  background-color: #f0f0f0;
  border: 3px solid gray;
  padding: 10px;
  margin: 5px;
  border-radius: 8px;
  cursor: pointer;
}

.swimmer-name {
  font-size: 1.5em;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
}
.confirmClicked {
  border-color: #4caf50; /* Vuetify-Grün */
}
</style>