import Vue from "vue";
import VueRouter from "vue-router";

import HomeView from "../views/HomeView.vue";
import Results from "../views/results.vue";
import LaneSelect from "../views/LaneSelect.vue";
import LaneMain from "@/views/LaneMain.vue";
import NotFound from "@/views/NotFound.vue";




Vue.use(VueRouter);

const router = new VueRouter({
    mode: "history",
    base: import.meta.env.BASE_URL,
    routes: [
        {
            path: "/", name: "home", component: HomeView,
        },
        {
            path: "/about", name: "about",
            // route level code-splitting
            // this generates a separate chunk (About.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import("../views/AboutView.vue"),
        },
        {path: '/results', component: Results},
        {path: '/lane', component: LaneSelect},
        {path: '/lane/:id', component: LaneMain},
        {path: "/:catchAll(.*)", component: NotFound},
    ],
});

export default router;
