import { createPinia } from "pinia";
import { pluginPinia } from "@/store/plugins";

const store = createPinia();

export function setupStore(app: any) {
    app.use(store);
}
store.use(pluginPinia);

export { store };
