<template>
<header class="bg-[#1b263b] text-white p-4">
    <div class="container mx-auto flex justify-between items-center">
      <div class="flex items-center space-x-4">
      <a href="/" class="button other">// Sett inn logo</a>
      <a href="/mapview" class="button other">📍 Kart</a>

    </div>
      <nav class="space-x-4">
        <a href="/" class="button other">Hjem</a>
        <a href="/about" class="button other">Om oss</a>
        <a href="/login" class="button login">Logg inn</a>
      </nav>
    </div>
  </header>
</template>

<script lang="ts" setup>
/**Når man trykker på kart, skal man komme til mapview.vue */
import { useRouter } from 'vue-router'
const router = useRouter()
const goToMapView = () => {
  router.push('/map')
}
const goToHome = () => {
  router.push('/')
}
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>

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
  margin: 30px 20px;

  /* Default farger hvis ingen variant settes */
  /*background-color: rgb(41, 35, 153);*/
  background-color: var(--light-blue);
  color: rgb(255, 255, 255);
}

/* Login-variant */
.button.login {
  background-color: var(--good-green); 
  border-color: var(--good-green);
  color: rgb(255, 255, 255);
}

.button.login:hover {
  background-color: rgb(255, 255, 255);
  border-color: var(--good-green);
  color: var(--good-green);
}

/* Other-variant */
.button.other {
  background-color: var(--light-blue);
  border-color: var(--light-blue);
  color: rgb(255, 255, 255);
}

.button.other:hover {
  background-color: rgb(255, 255, 255);
  border-color: var(--light-blue);
  color: var(--darkest-blue);
}

</style>
