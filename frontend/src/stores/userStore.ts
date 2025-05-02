import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    role: "",
  }),

  persist: {
    storage: sessionStorage,
  },

  actions: {
    logout() {
      this.role = "";
    },

    setRole(role: string) {
      this.role = role;
    },
  },
});
