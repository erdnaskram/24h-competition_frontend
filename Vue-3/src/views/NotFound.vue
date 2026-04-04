<template>
  <v-container class="py-12 text-center">
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <p class="text-h1 font-weight-bold" style="color: #013157;">404</p>
        <p class="text-h5 font-weight-medium mb-2" style="color: #013157;">Seite nicht gefunden</p>
        <p class="text-body-1 text-medium-emphasis mb-6">
          Die gesuchte Seite wurde möglicherweise entfernt, umbenannt oder ist vorübergehend nicht verfügbar.
        </p>
        <v-btn color="primary" prepend-icon="mdi-home" :to="'/'">
          Zur Startseite
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "NotFound",
}
</script>
