<template>
  <div class="beredskap-page">
    <Header />

    <main class="main-content">
      <div class="info-header">
        <h1>Min beredskap</h1>
        <p>Her kan du sjekke din beredskap.</p>
      </div>

      <div class="grid-container">

        <router-link to="/household" class="box registered">
          <h3>Husstand</h3>
          <p>Se oversikt over medlemmene i din husstand.</p>
        </router-link>

        <router-link to="/storage" class="box registered">
          <h3>Beredskapslager</h3>
          <p>Se om du har nok mat og drikke til en eventuell krisesituasjon.</p>
        </router-link>

        <router-link to="/notes" class="box registered">
        <h3>Erfaringer fra en krise?</h3>
        <p>Skriv et notat fra en krise du har opplevd, og del den med andre!</p>
        </router-link>

      </div>

      <div class="info-header">
        <h1>Andre tjenester</h1>
      </div>

      <div class="grid-container">
      <router-link to="/map" class="box">
        <h3>Finn tilfluktsrom</h3>
        <p>Se oversikt over tilfluktsrom i nærheten.</p>
      </router-link>


      <router-link to="/news" class="box">
        <h3>Nyheter</h3>
        <p>Hold deg oppdatert på relevante hendelser.</p>
      </router-link>

      <router-link to="/map" class="box">
        <h3>Tjenestekart</h3>
        <p>Se hvor du finner hjelp i ditt område.</p>
      </router-link>
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

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.beredskap-page {
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
  margin: 4px 125px 20px 125px;
  text-align: center;
}

.info-header h1 {
  margin: 0 0 16px 0;
  font-size: 2.5rem;
  font-weight: bold;
}

.info-header p {
  margin: 0;
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
  margin: 20px 100px;
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

.box.registered {
  background-color: var(--good-green);
  border: 2px solid var(--good-green);
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

.box.registered:hover {
  background-color: var(--good-green);
  color: white;
  transform: scale(1.02);
  cursor: pointer;
}
</style>
