import { createPinia } from "pinia";
import { createApp } from "vue";
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'
import ToastPlugin from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-bootstrap.css';

import App from "./App.vue";
import "./assets/tailwind.css";
import "./assets/base.css"
import "./assets/main.css"
import router from "./router/index.ts";

const app = createApp(App);
const pinia = createPinia();

pinia.use(piniaPluginPersistedState)

app.use(pinia);
app.use(ToastPlugin)


app.use(router);
app.mount("#app");
