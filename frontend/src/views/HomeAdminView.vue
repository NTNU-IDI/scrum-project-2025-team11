<template>
  <div class="admin-page">
    <Header />

    <main class="main-content">
      <div class="info-header">
        <h1>Administrasjon av krisefikseren.no</h1>
        <p>Husk at du gjennom dette grensesnittet har tilgang til kritiske opplysninger.</p>
        <p>Vennligst ta ansvaret som følger med det.</p>
      </div>

      <div class="grid-container">
        <div class="box">
          <h3>Krisehendelser</h3>
          <p>Her kan du administrere opplysninger om krisehendelse.</p>
        </div>
        <div class="box">
          <h3>Kartopplysninger</h3>
          <p>Her kan du administrere opplysninger om kartet.</p>
        </div>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script lang="ts" setup>
import Header from '@/components/HeaderInloggedComponent.vue'
import Footer from '@/components/Footer.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goToHome = () => {
  router.push('/')
}

const goToLogout = () => {
  router.push('/')
}
</script>

<style scoped>
.admin-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: white;
}

.main-content {
  flex: 1;
  padding: 48px 32px;
  background-color: white;
}

.info-header {
  background-color: #ffffff;
  color: var(--darkest-blue);
  padding: 24px;
  border-radius: 8px;
  margin: 40px 125px;
  text-align: center;
}

.info-header h1 {
  margin: 0 0 16px 0;
  font-size: 2.5rem;
  font-weight: bold;
}

.info-header p {
  margin: 8px 0 0 0;
  font-size: 1.5rem;
  font-weight: 500;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  padding: 40px;
  background-color: #ffffff;
  border-radius: 8px;
  margin: 40px 100px;
}

.box {
  background-color: var(--light-blue);
  border: 2px solid var(--light-blue);
  color: white;
  padding: 24px;
  border-radius: 8px;
  transition: background-color 0.4s ease, transform 0.4s ease;
  box-shadow: 0 4px 6px rgba(0.5, 0, 0, 0.5);
  text-align: center;
}

.box h3 {
  margin: 0 0 8px 0;
  font-size: 1.5rem;
  font-weight: bold;
}

.box p {
  margin: 0;
  font-size: 1rem;
}

.box:hover {
  background-color: rgb(169, 184, 203);
  color: var(--darkest-blue);
  transform: scale(1.02);
  cursor: pointer;
}
</style>
