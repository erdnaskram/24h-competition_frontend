import {createApp} from 'vue'
import {createPinia} from 'pinia';
import './style.css'
import App from './App.vue'
import router from './router'
import './assets/global.css';
import {createBootstrap} from 'bootstrap-vue-next'
import { vuetify } from './plugins/vuetify';
import '@mdi/font/css/materialdesignicons.css'

// Add the necessary CSS
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

const app = createApp(App);
const boostrap = createBootstrap();
const pinia = createPinia();

app.use(router);
app.use(pinia);
app.use(boostrap);
app.use(vuetify);

app.mount('#app');
