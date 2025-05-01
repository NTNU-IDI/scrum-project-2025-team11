<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />
      </div>

      <div class="header-right">
        <img src="../assets/ikon/house-line-fill.png" alt="Home" class="icon" @click="goToHousehold" />
        <img src="../assets/ikon/list-plus-fill.png" alt="Home" class="icon" @click="goToStorage" />


        <a href="/" class="button login" @click.prevent="goToLogout">Logg ut</a>
      </div>
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
const goToStorage = () => {
  router.push('/storage')
}
const goToHousehold = () => {
  router.push('/household')
}
</script>

<style scoped>
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

.header-left {
  display: flex;
  align-items: center;
  gap: 32px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 32px;
}

.logo {
  height: 100px;
  width: auto;
  cursor: pointer;
}

.icon {
  height: 55px;
  width: 55px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.icon:hover {
  transform: scale(1.1);
}

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
  margin: 10px 10px;
  background-color: var(--light-blue);
  color: #ffffff;
}

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
