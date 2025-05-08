import axios from "axios";
import {useUserStore} from "@/stores/userStore.ts";
import {useCredentialStore} from "@/stores/credentialStore.ts";
import {useRouter} from "vue-router";
import {useToast} from "vue-toast-notification";
import router from "@/router";

const $toast = useToast()

export async function confirm2fa(code: string) {
    const credentialStore = useCredentialStore()
    const userStore = useUserStore()
    if (credentialStore.username === '' || credentialStore.password === '') {
        router.push("/login")
        let instance = $toast.error("Nettsiden støtte på et problem med to-faktor-autentiseringen. Vennligst forsøk å logge inn på nytt.")
        return
    }

    await axios.post("http://localhost:8080/auth/confirm-authentication", {
        twoFactorCode: {
            code: code
        },
        login: {
            username: credentialStore.username,
            password: credentialStore.password
        }
    }, {
        withCredentials: true
    })
        .then((response) => {
            userStore.setRole(response.data.role)
            router.push("/")
        })
        .catch((error) => {
            userStore.logout()
            router.push("/login")
            let instance = $toast.error("Noe gikk galt under autentiseringen. Vennligst forsøk å logge inn på nytt.")
        })
}


export async function login(username: string, password: string) {
    const credentialStore = useCredentialStore()

    await axios.post("http://localhost:8080/auth/login", {
        username: username,
        password: password
    }, {
        withCredentials: true
    })
        .then(() => {
            credentialStore.setCredentials(username, password)
            router.push("/auth")
        })
        .catch((error) => {
            if (error.status === 400) {
                let instance = $toast.error("Kunne ikke logge inn med gitt brukernavn og passord. " +
                    "Forsikre deg om at de er riktige og prøv igjen.")
            } else if (error.status === 500) {
                let instance = $toast.error("Serveren møtte på et uventet problem. Vennligst vent og prøv igjen senere.")
            } else {
                let instance = $toast.error("Noe uventet har oppstått. Vennligst vent og prøv igjen senere.")
            }
        });
}

export async function registerNormalUser(firstName: string, lastName: string, username: string, email: string,
                                          password: string, householdId: number) {
    const credentialStore = useCredentialStore()

    await axios.post("http://localhost:8080/auth/register", {
        firstName: firstName,
        lastName: lastName,
        username: username,
        email: email,
        password: password,
        householdId: householdId
    }, {
        withCredentials: true
    })
        .then((response) => {
            credentialStore.setCredentials(username, password)
            router.push("/auth")
        })
        .catch((error) => {
            if (error.status === 409) {
                let instance = $toast.error("Brukernavn eller epost allrede i bruk. Vennligst prøv noe annet.")
            } else {
                let instance = $toast.error("Noe uventet har oppstått. Vennligst vent og prøv igjen senere.")
            }
        });
}

export async function refreshToken() {
    const userStore = useUserStore()
    const router = useRouter()

    await axios.post("http://localhost:8080/auth/refresh", {},{withCredentials: true})
        .then((response) => {
            userStore.setRole(response.data.role)
        })
        .catch(() =>{
            userStore.logout()
            router.push("/")
            let instance = $toast.error("Du har blitt logget ut grunnet utgått sesjon.")
            throw new Error("Refresh token failure.")
        });
}

export function logOutUser() {
    const userStore = useUserStore()
    const router = useRouter()

    userStore.logout()
    router.push("/")
}