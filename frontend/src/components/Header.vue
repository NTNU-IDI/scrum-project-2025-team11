<template>
  <header class="header">
    <div class="header-container">
      <div class="header-icons">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />

        <img src="../assets/ikon/house-line-fill.png" alt="Home" class="icon" @click="goToHome" />

        <img src="../assets/ikon/map-trifold-fill.png" alt="Map" class="icon" @click="goToMap" />

        <img src="../assets/ikon/lightbulb-fill.png" alt="Information" class="icon" @click="goToInformation" />
      </div>

      <nav class="nav-buttons">
        <a href="/login" class="button login">Logg inn</a>
      </nav>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goToMap = () => {
  router.push('/map')
}
const goToHome = () => {
  router.push('/')
}
const goToLogin = () => {
  router.push('/login')
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

/* --- Logo and icons --- */
.header-icons {
  display: flex;
  align-items: center;
  gap: 60px;
}

.logo {
  height: 100px;
  width: auto;
  cursor: pointer;
}

/* Alle ikoner */
.icon {
  height: 55px;
  width: 55px;
  cursor: pointer;
}

/* --- Navigasjonsknapper --- */
.nav-buttons {
  display: flex;
  gap: 32px; /* tilsvarer space-x-8 */
}

/* --- Knappestil --- */
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
  background-color: var(--light-blue);
  color: #ffffff;
}

/* Login-variant */
.button.login {
  background-color: var(--good-green);
  border-color: var(--good-green);
  color: #ffffff;
}

.button.login:hover {
  background-color: #ffffff;
  border-color: var(--good-green);
  color: var(--good-green);
}

/* Other-variant */
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
