import './assets/main.css'

import {createApp} from 'vue'
import router from './router.js'
import App from './App.vue'
import { BootstrapVue3} from "bootstrap-vue-3";

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

createApp(App)
    .use(router)
    .use(BootstrapVue3)
    .mount('#app')
