<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />
        <a href="/" class="button other">Hjem</a>
      </div>

      <nav class="nav-buttons">
        <img src="../assets/ikon/lightbulb-fill.png" alt="Information" class="icon" @click="goToInformation" />
        <a href="/" class="button login">Logg ut</a>
      </nav>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goToHome = () => {
  router.push('/')
}
const goToLogout = () => {
  router.push('/')
}
const goToInformation = () => {
  router.push('/information')
}
</script>

<style scoped>
/* --- Header styling --- */
.header {
  background-color: #1b263b;
  color: white;
  padding: 16px;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* --- Venstre del: logo + "Hjem" --- */
.header-left {
  display: flex;
  align-items: center;
  gap: 32px; /* tidligere space-x-4, nå tydeligere kontrollert */
}

.logo {
  height: 100px;
  width: auto;
  cursor: pointer;
}

/* --- Navigasjonsknapper --- */
.nav-buttons {
  display: flex;
  gap: 32px; /* tilsvarer tidligere space-x-4 */
}

/* --- Felles knappestil --- */
.button {
  display: inline-block;
  padding: 10px 20px;
  font-size: 18px;
  border-radius: 4px;
  border: 2px solid transparent;
  text-align: center;
  cursor: pointer;
  transition: all 0.4s ease;
  text-decoration: none;
  font-weight: 500;
  margin: 10px 10px; /* justert litt ned for bedre sentrering */
  background-color: var(--light-blue);
  color: #ffffff;
}

/* --- Logout-knapp (rød variant) --- */
.button.login {
  background-color: var(--bad-red);
  border-color: var(--bad-red);
  color: #ffffff;
}

.button.login:hover {
  background-color: #ffffff;
  border-color: var(--bad-red);
  color: var(--bad-red);
}

/* --- Other-knapp (f.eks. "Hjem") --- */
.button.other {
  background-color: var(--light-blue);
  border-color: var(--light-blue);
  color: #ffffff;
}

.button.other:hover {
  background-color: #ffffff;
  border-color: var(--light-blue);
  color: var(--darkest-blue);
}
</style>
