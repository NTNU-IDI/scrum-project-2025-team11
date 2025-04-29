<template>
  <footer class="footer">
    <div class="footer-content">
      <h1>Andre tjenester</h1>

      <div class="footer-links">
        <a href="/about">Om oss</a> |
        <a href="/privacy">Personvern</a> |
        <a href="/userhome">User home (for testing)</a> |
        <a href="/admin">Admin (for testing)</a> |
        <button @click="openPDF" class="link-button">Personvernerklæring</button>
      </div>
    </div>
  </footer>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goToHome = () => {
  router.push('/')
}
const goToUserHome = () => {
  router.push('/userhome')
}
const goToAdmin = () => {
  router.push('/admin')
}
const openPDF = () => {
  window.open('../assets/privacy_page.pdf', '_blank')
}
</script>

<style scoped>
.footer {
  background-color: var(--darkest-blue);
  color: white;
  padding: 20px;
}

/** The footer title  h1*/
.footer h1 {
  font-size: 32px;
  font-weight: bold;
  text-align: left;
  margin-bottom: 12px;
  color: white;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-content h1 {
  font-size: 20px;
  font-weight: bold;
  text-align: left;
  margin-bottom: 12px;
}

.footer-links {
  font-size: 14px;
  text-align: left;
  margin-top: 8px;
}

.footer-links a {
  color: white;
  text-decoration: none;
  margin-right: 12px;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #d1d5db;
}

.link-button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 14px;
  margin-left: 12px;
  transition: color 0.3s ease;
  padding: 0;
}

.link-button:hover {
  color: #d1d5db;
}
</style>
