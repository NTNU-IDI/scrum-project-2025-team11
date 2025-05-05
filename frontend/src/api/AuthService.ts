import axios from "axios";
import {useUserStore} from "@/stores/userStore.ts";
import {useRouter} from "vue-router";
import router from "@/router";


export async function login(username: string, password: string) {
    const userStore = useUserStore()

    await axios.post("http://localhost:8080/auth/login", {
        username: username,
        password: password
    })
        .then((response) => {
            userStore.setRole(response.data.role)
            router.push("/")
        })
        .catch((error) => {
            //console.log(error)
            if (error.status === 401) {
                alert("Kunne ikke logge inn med gitt brukernavn og passord. " +
                    "Forsikre deg om at de er riktige og prøv igjen.")
            } else if (error.status === 500) {
                alert("Serveren møtte på et uventet problem. Vennligst vent og prøv igjen senere.")
            } else {
                alert("Noe uventet har oppstått. Vennligst vent og prøv igjen senere.")
            }
        });
}

export async function registerNormalUser(firstName: string, lastName: string, username: string, email: string,
                                          password: string, householdId: number) {
    const userStore = useUserStore()

    await axios.post("http://localhost:8080/auth/register", {
        firstName: firstName,
        lastName: lastName,
        username: username,
        email: email,
        password: password,
        householdId: householdId
    })
        .then((response) => {
            userStore.setRole(response.data.role)
            router.push("/")
        })
        .catch((error) => {
            if (error.status === 409) {
                alert("Brukernavn eller epost allrede i bruk. Vennligst prøv noe annet.")
            } else {
                alert("Noe uventet har oppstått. Vennligst vent og prøv igjen senere.")
            }
        });
}

export async function refreshToken() {
    const userStore = useUserStore()
    const router = useRouter()

    await axios.post("http://localhost:8080/auth/refresh")
        .then((response) => {
            userStore.setRole(response.data.role)
        })
        .catch(() =>{
            userStore.logout()
            alert("Du har blitt logget ut grunnet utgått sesjon.")
            router.push("/")
        });
}

export function logOutUser() {
    const userStore = useUserStore()
    const router = useRouter()

    userStore.logout()
    router.push("/")
}