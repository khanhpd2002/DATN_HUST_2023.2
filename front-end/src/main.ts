import { createApp } from "vue";
import "vue-toastification/dist/index.css";
import { toast } from "vue3-toastify";
import "vue3-toastify/dist/index.css";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import i18n from "@/locales/index";
import { setupStore } from "@/store";
import mitt from "mitt";
import * as ACCDComponents from "@cd/design-system";
import "@/assets/scss/table.css";
import VueCryptojs from "vue-cryptojs";
import "@cd/design-system/dist/styles.css";
import "@/assets/scss/style.css";


import { initPlugins } from "@cd/design-system";

const pinia = createPinia();

const app = createApp(App)
    .use(pinia)
    .use(VueCryptojs)
    .use(router);

    initPlugins(app)

Object.keys(ACCDComponents).forEach((componentName) => {
    if (componentName == "CDInput") {
        app.component(
            "ACCDInputText",
            ACCDComponents[componentName as keyof typeof ACCDComponents].Text
        );
        app.component(
            "ACCDInputNumber",
            ACCDComponents[componentName as keyof typeof ACCDComponents].Number
        );
    } else {
        app.component(
            "AC" + componentName,
            ACCDComponents[componentName as keyof typeof ACCDComponents]
        );
    }
});

export const $toast = function (message: string, isSuccess: boolean) {
    if (isSuccess) {
        toast["success"](message, {
            autoClose: 1000,
        });
        return;
    }
    toast["error"](message, {
        autoClose: 1000,
    });
};
app.config.globalProperties.$toast = $toast;
let currentLang = "vn"; //app.config.globalProperties.$langStore.$state.lang

export const instanceI18n = i18n(currentLang) as any;
// localStorage.setItem("inventoryId", "1");

app.config.globalProperties.$t = instanceI18n.global.t;
setupStore(app);
app.config.globalProperties.$store = {};

app.config.globalProperties.$evtBus = mitt();

router.isReady().then(() => {
    app.mount("#app");
});
