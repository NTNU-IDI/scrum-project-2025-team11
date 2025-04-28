import { defineStore } from 'pinia'

import { getJwtTokenFromLogin, getJwtTokenAfterRegistration } from '../utils/authService'

export const useUserStore = defineStore('token', {
    state: () => ({
        jwtToken: '',
        username: '',
    }),

    persist: {
        storage: sessionStorage,
    },

    actions: {
        async register(username: string, email: string, password: string, repeatedPassword: string){
            try {
                await getJwtTokenAfterRegistration(username, email, password, repeatedPassword).then((token) => {
                    if (token) {
                        this.jwtToken = token;
                    } else {
                        console.log('Failed to retrieve token');
                    }

                })
            } catch(error) {
                console.log('Error: ' + error);
            }
            this.username = username;
        },

        async login(username: string, password: string) {
            try {
                await getJwtTokenFromLogin(username, password).then((token) => {
                    if (token) {
                        this.jwtToken = token;
                    } else {
                        console.log('Failed to retrieve token');
                    }
                })
            } catch(error) {
                console.log('Error: ' + error);
            }
            this.username = username;
        },

        getToken() {
            let isExpired = this.isTokenExpired();

            if(isExpired) {
                //jwtToken is set to '' when logged out
                this.logout()
            }

            return this.jwtToken;
        },

        logout() {
            this.jwtToken = '';
            this.username = '';
        },

        isTokenExpired() {
            if (!this.jwtToken) {
                return true;
            }

            const payload = JSON.parse(atob(this.jwtToken.split('.')[1]));
            return payload.exp < Date.now() / 1000;
        }
    }
})
