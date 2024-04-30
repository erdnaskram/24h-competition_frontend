import { createWebHistory, createRouter } from 'vue-router'

import HomeView from './components/views/HomeView.vue'
import Results from './components/views/results.vue'
import LaneSelect from './components/views/LaneSelect.vue'
import NotFound from "@/components/views/NotFound.vue"
import LaneMain from "@/components/views/LaneMain.vue"

const routes = [
        { path: '/', component: HomeView },
        { path: '/results', component: Results },
        { path: '/lane', component: LaneSelect },
        { path: '/lane/:id', component: LaneMain },
    {
        path: "/:catchAll(.*)",
        component: NotFound,
    },
    ]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router