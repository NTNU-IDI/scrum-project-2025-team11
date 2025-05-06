import { defineStore } from "pinia";

export const useCredentialStore = defineStore("credential", {
    state: () => ({
        username: "",
        password: ""
    }),

    actions: {
        setCredentials(username: string, password: string) {
            this.username = username
            this.password = password
        },
        clearCredentials() {
            this.username = ""
            this.password = ""
        }
        ,
    },
});
