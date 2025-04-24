import { createPinia } from "pinia";
import piniaPersist from 'pinia-plugin-persistedstate'
import { createApp } from "vue";
import App from "./App.vue";
import "./assets/tailwind.css";
import router from "./router/index.ts";

const app = createApp(App);

const pinia = createPinia()
pinia.use(piniaPersist)
app.use(createPinia());

app.use(router);
app.mount("#app");
