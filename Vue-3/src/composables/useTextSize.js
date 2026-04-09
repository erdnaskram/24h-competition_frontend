import { ref, computed, watch } from 'vue';

const LEVELS = [1, 1.2, 1.5]; // 100%, 120%, 150%
const CLASSES = ['', 'text-size-120', 'text-size-150'];

const stored = parseInt(localStorage.getItem('textSizeLevel') ?? '0', 10);
const textSizeLevel = ref(LEVELS.includes(LEVELS[stored]) ? stored : 0);

function applyClass() {
  document.documentElement.classList.remove('text-size-120', 'text-size-150');
  const cls = CLASSES[textSizeLevel.value];
  if (cls) document.documentElement.classList.add(cls);
}

applyClass();

watch(textSizeLevel, (val) => {
  localStorage.setItem('textSizeLevel', String(val));
  applyClass();
});

export function useTextSize() {
  const textSizeLabel = computed(() => ['100%', '120%', '150%'][textSizeLevel.value]);
  return {
    textSizeLevel,
    textSizeLabel,
    cycleTextSize: () => { textSizeLevel.value = (textSizeLevel.value + 1) % 3; },
  };
}
