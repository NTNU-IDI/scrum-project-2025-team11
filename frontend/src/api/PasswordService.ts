import axios from "axios";
import router from "@/router";
import {useToast} from "vue-toast-notification";

const $toast = useToast()

export async function sendEmailForReset(email: string) {
    await axios.post("http://localhost:8080/api/auth/request-password-reset", {
        email: email
    }, {
        withCredentials: true
    })
        .then(() => {
            router.push("/reset")
            let instance = $toast.info("En epost med bekreftelseskode har blitt sendt til " + email + ".")
        })
        .catch((error) => {
            if (error.response.status === 500) {
                sendToLoginAfterError()
            } else {
                //To prevent exposing emails in use, user will be sent to reset page either way
                router.push("/reset")
                let instance = $toast.info("En epost med bekreftelseskode har blitt sendt til " + email + ".")
            }
    })
}

export async function resetPassword(code: string, newPassword: string) {
    await axios.post("http://localhost:8080/api/auth/reset-password", {
        token: code,
        newPassword: newPassword
    }, {
        withCredentials: true
    })
        .then(() => {
            router.push("/login")
            let instance = $toast.success("Passordet har blitt oppdatert!")
        })
        .catch((error) => {
            router.push("/login")
            let instance = $toast.error("Fikk ikke til å oppdatere passord. Vennligst prøv igjen.")
        })
}

function sendToLoginAfterError() {
    router.push("/login")
    let instance = $toast.error("Et problem oppsto. Vennligst prøv igjen.")
}