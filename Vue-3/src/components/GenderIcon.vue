<template>
  <v-icon :size="size" :style="{ color: config.color }" :title="config.label">{{ config.icon }}</v-icon>
</template>

<script>
export default {
  name: 'GenderIcon',
  props: {
    gender: { type: String, default: '' },
    size:   { type: [String, Number], default: 16 },
  },
  computed: {
    config() {
      const g = (this.gender || '').toLowerCase();
      if (g === 'm' || g === 'male')              return { icon: 'mdi-gender-male',       color: '#1565C0', label: 'männlich' };
      if (g === 'f' || g === 'w' || g === 'female') return { icon: 'mdi-gender-female',     color: '#C62828', label: 'weiblich' };
      return                                             { icon: 'mdi-gender-non-binary',  color: '#6A1B9A', label: 'divers'   };
    },
  },
};
</script>
