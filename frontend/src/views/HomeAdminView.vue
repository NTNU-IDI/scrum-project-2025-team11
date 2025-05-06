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

      <div class="grid-container">
        <router-link to="/map" class="box registered">
          <h3>Krisehendelser</h3>
          <p>Her kan du legge til, fjerne og redigere krisehendelser. 
            Merk at når du bekrefter en endring, vil disse vises for alle brukere.</p>
        </router-link>
        
        <router-link to="/map" class="box registered">
          <h3>Kartopplysninger og punkter</h3>
          <p>Her kan du administrere de ulike punktene/posisjonene på kartet ved å trykke på dem.</p>
        </router-link>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script lang="ts" setup>
import Header from '@/components/HeaderAdminComponent.vue'
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

