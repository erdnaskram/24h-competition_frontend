import {createApp} from 'vue'
import {createPinia} from 'pinia';
import './style.css'
import App from './App.vue'
import router from './router'
import './assets/global.css';
import { vuetify } from './plugins/vuetify';
import '@mdi/font/css/materialdesignicons.css'

const app = createApp(App);
const pinia = createPinia();

app.use(router);
app.use(pinia);
app.use(vuetify);

app.mount('#app');
