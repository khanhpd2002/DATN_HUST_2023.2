import { createRouter, createWebHistory } from "vue-router";
import middlewarePipeline from "./middleware/middlewarePipeline";
import routes from "./route";

const router = createRouter({
    history: createWebHistory(""),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition;
        } else {
            return { top: 0 };
        }
    },
});
router.onError((error, to) => {
    if (error.message.includes('Failed to fetch dynamically imported module') || error.message.includes("Importing a module script failed")) {
        window.location.href = to.fullPath
    }
})
router.beforeEach((to, from, next) => {
    // const titleText = to.name as string;
    // const words = titleText.split(" ");
    // const wordslength = words.length;
    // for (let i = 0; i < wordslength; i++) {
    //     words[i] = words[i][0].toUpperCase() + words[i].substr(1);
    // }
    document.title = "GMS - Chủ Gara";

    /** Navigate to next if middleware is not applied */
    if (!to.meta.middleware) {
        return next();
    }

    const middleware = to.meta.middleware as any[];
    const context = { to, from, next };
    return middleware[0]({
        ...context,
        next: middlewarePipeline(context, middleware, 1),
    });
});

router.afterEach(() => {
    // Remove initial loading
    console.log(document.fonts)
    const appLoading = document.getElementById("loading-bg");
    if (appLoading) {
        appLoading.style.display = "none";
    }
});

export default router;
