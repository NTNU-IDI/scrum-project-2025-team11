import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    role: "super_admin",
  }),

  persist: {
    storage: localStorage,
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
