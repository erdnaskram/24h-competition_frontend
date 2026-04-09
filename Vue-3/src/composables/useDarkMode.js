import { computed, watch } from 'vue';
import { useTheme } from 'vuetify';

function applyHtmlClass(dark) {
  document.documentElement.classList.toggle('dark-mode', dark);
}

// Apply on initial load before any component mounts
applyHtmlClass(localStorage.getItem('theme') === 'dark');

export function useDarkMode() {
  const theme = useTheme();
  const isDark = computed(() => theme.global.name.value === 'dark');

  watch(isDark, (val) => applyHtmlClass(val), { immediate: true });

  function toggleDarkMode() {
    theme.global.name.value = isDark.value ? 'light' : 'dark';
    localStorage.setItem('theme', theme.global.name.value);
  }

  return { isDark, toggleDarkMode };
}
