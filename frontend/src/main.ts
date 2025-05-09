import { createPinia } from "pinia";
import { createApp } from "vue";
import axios from 'axios'
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'
import ToastPlugin from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-bootstrap.css';
import {useToast} from "vue-toast-notification";
import {refreshToken} from "@/api/AuthService.ts";
import {VueReCaptcha} from "vue-recaptcha-v3";

import App from "./App.vue";
import "./assets/tailwind.css";
import "./assets/base.css"
import "./assets/main.css"
import router from "./router/index.ts";
import LogInComponent from "./components/LogInComponent.vue";
import RegisterComponent from "./components/RegisterComponent.vue";

const app = createApp(App);
const pinia = createPinia();
const $toast = useToast();

pinia.use(piniaPluginPersistedState)

app.use(pinia);
app.use(ToastPlugin);
app.use(router);
app.use(VueReCaptcha, {
    siteKey: import.meta.env.VITE_RECAPTCHA_SITE_KEY,
    loaderOptions: {
        autoHideBadge: false,
        explicitRenderParameters: {
            badge: "bottomright",
            theme: "light",
        },
    },
});
app.mount("#app");



axios.interceptors.response.use((response) => {
    return response
}, async (error) => {
    if (error.response) {
        if (error.response.status === 401) {
            try {
                await refreshToken()
                const config = {...error.config, withCredentials: true};
                return axios(config);
            } catch (refreshError) {
                return Promise.reject(refreshError)
            }
        } else if (error.response.status === 403) {
            let instance = $toast.error("Du er ikke autorisert til denne funksjonen.")
        }
    }
    return Promise.reject(error)
})
