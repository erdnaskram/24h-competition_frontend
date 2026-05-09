import { ref, computed, watch } from 'vue';

const LEVELS = [0.8, 1, 1.2, 1.5]; // 80%, 100%, 120%, 150%
const CLASSES = ['text-size-80', '', 'text-size-120', 'text-size-150'];

// Migration: alter Index 0 war 100%, neuer Index 0 ist 80% → um 1 verschieben
if (localStorage.getItem('textSizeLevel') === '0') localStorage.setItem('textSizeLevel', '1');
const stored = parseInt(localStorage.getItem('textSizeLevel') ?? '1', 10);
const textSizeLevel = ref(LEVELS.includes(LEVELS[stored]) ? stored : 1);

function applyClass() {
  document.documentElement.classList.remove('text-size-80', 'text-size-120', 'text-size-150');
  const cls = CLASSES[textSizeLevel.value];
  if (cls) document.documentElement.classList.add(cls);
}

applyClass();

watch(textSizeLevel, (val) => {
  localStorage.setItem('textSizeLevel', String(val));
  applyClass();
});

export function useTextSize() {
  const textSizeLabel = computed(() => ['80%', '100%', '120%', '150%'][textSizeLevel.value]);
  return {
    textSizeLevel,
    textSizeLabel,
    cycleTextSize: () => { textSizeLevel.value = (textSizeLevel.value + 1) % 4; },
  };
}
