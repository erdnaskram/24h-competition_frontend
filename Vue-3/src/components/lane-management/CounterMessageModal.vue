<template>
  <BButton variant="success" @click="showMessageModal = true">Message</BButton>
  <BModal v-model="showMessageModal" title="Nachricht an den Bahnen-Einteiler" :hide-footer="true">
    <div id="dialog2" title="Nachricht senden">
      <p>
        <BButton id="pause" style="width: 100%">Brauche Ablösung (Pipi-Pause)</BButton>
      </p>
      <p>
        <BButton id="question" style="width: 100%">Habe eine Frage (bitte vorbeikommen)</BButton>
      </p>
    </div>
  </BModal>
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