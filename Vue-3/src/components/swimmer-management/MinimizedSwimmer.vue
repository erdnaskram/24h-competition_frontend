<template>
  <v-card class="minimized-swimmer">
    <div class="swimmer-row">
      <a class="swimmer-name">
        {{ swimmer.id }} - {{ swimmer.swimmerName.first }} {{ swimmer.swimmerName.last }} ({{ swimmer.age }}J, <gender-icon :gender="swimmer.gender" :size="16" />)
      </a>
      <v-btn
          icon="mdi-eye-off"
          color="blue"
          size="small"
          flex-shrink="0"
          @click.stop="showActiveSwimmer"
          title="Einblenden"
      />
    </div>
  </v-card>
</template>

<script>
import GenderIcon from '../GenderIcon.vue';
export default {
  name: "minimizedSwimmer",
  components: { GenderIcon },
  props: {
    swimmer: {
      type: Object,
      required: true,
    },
  },
  methods: {
    showActiveSwimmer() {
      this.$emit('showActiveSwimmer', this.swimmer);
    },
  },
}
</script>

<style scoped>
.minimized-swimmer {
  user-select: none;
  background-color: #f0f0f0;
  padding: 10px;
  margin: 5px;
  border-radius: 8px;
}

.swimmer-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.swimmer-name {
  font-size: 1.2rem;
  font-weight: bold;
  color: #013157;
  text-decoration: none;
  flex: 1;
  min-width: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>