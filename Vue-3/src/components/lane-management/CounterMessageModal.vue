<template>
  <v-btn color="green" @click="showMessageModal = true">Message</v-btn>
  <v-dialog v-model="showMessageModal" title="Nachricht an den Bahnen-Einteiler" :hide-footer="true">
    <div id="dialog2" title="Nachricht senden">
      <p>
        <v-btn id="pause" style="width: 100%">Brauche Ablösung (Pipi-Pause)</v-btn>
      </p>
      <p>
        <v-btn id="question" style="width: 100%">Habe eine Frage (bitte vorbeikommen)</v-btn>
      </p>
    </div>
  </v-dialog>
</template>

<script>
export default {
  name: "CounterMessageModal",
  data() {
    return {
      showMessageModal: false,
    };
  },
}
</script>

<style scoped>

</style>