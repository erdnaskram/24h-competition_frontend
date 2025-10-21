import '../src/assets/main.css'

import {createApp} from 'vue'
import router from '../src/router/index.js'
import App from './App.vue'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

const app = createApp(App);

app.use(router);
app.use(BootstrapVue3);

app.mount('#app');
