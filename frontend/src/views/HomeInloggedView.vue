<template>
  <div class="page-wrapper">
    <Header />

    <main class="main-content">
      <div class="info-header">
        <h1>Min beredskap</h1>
        <p>Her kan du sjekke din beredskap.</p>
      </div>

      <div class="grid-container">
        <router-link to="/household" class="box registered">
          <h3>Husstand</h3>
          <p>Se oversikt over medlemmene i husstanden din.</p>
        </router-link>

        <router-link to="/storage" class="box registered">
          <h3>Beredskapslager</h3>
          <p>Sjekk om du har nok forsyninger til en krise.</p>
        </router-link>

        <router-link to="/notes" class="box registered">
          <h3>Erfaringer</h3>
          <p>Del notater og erfaringer fra kriser du har opplevd.</p>
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
</script>
