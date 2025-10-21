import { createRouter, createWebHistory } from 'vue-router';

import HomeView from '../views/HomeView.vue';
import Results from '../views/Results.vue';
import LaneSelect from '../views/LaneSelect.vue';
import LaneMain from '../views/LaneMain.vue';
import CheckIn  from "../views/CheckIn.vue";
import NotFound from '../views/NotFound.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'home', component: HomeView },
        { path: '/results', component: Results },
        { path: '/lane', component: LaneSelect },
        { path: '/lane/:id', component: LaneMain },
        { path: '/checkin', component: CheckIn },
        { path: '/:catchAll(.*)', component: NotFound },
    ],
});

export default router;