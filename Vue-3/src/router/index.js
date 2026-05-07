import { createRouter, createWebHistory } from 'vue-router';

import AdminView from '../views/AdminView.vue';
import Results   from '../views/Results.vue';
import LaneSelect from '../views/LaneSelect.vue';
import LaneMain from '../views/LaneMain.vue';
import CheckIn  from "../views/CheckIn.vue";
import NotFound from '../views/NotFound.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/',         name: 'lane', component: LaneSelect, meta: { title: 'Bahnauswahl' } },
        { path: '/lane',                   component: LaneSelect, meta: { title: 'Bahnauswahl' } },
        { path: '/lane/:id',               component: LaneMain,   meta: { title: 'Bahn' } },
        { path: '/checkin',                component: CheckIn,    meta: { title: 'Check-In' } },
        { path: '/results',                component: Results,    meta: { title: 'Ergebnisse' } },
        { path: '/admin',                  component: AdminView,  meta: { title: 'Admin' } },
        { path: '/:catchAll(.*)',          component: NotFound },
    ],
});

router.afterEach((to) => {
    const base = 'DLRG 24h'
    if (to.params.id) {
        document.title = `Bahn ${to.params.id} – ${base}`
    } else {
        document.title = to.meta.title ? `${to.meta.title} – ${base}` : base
    }
})

export default router;