<template>
  <div class="page-wrapper">
    <HeaderBase />

    <main class="main-content">
      <div class="info-header">
        <h1>Velkommen til Krisefikseren!</h1>
      </div>

      <div class="info-header">
        <p>Vet du hva du bør gjøre FØR, UNDER og ETTER en krise?</p>
      </div>

      <div class="grid-container">
        <div class="box">
          <h3>FØR</h3>
          <p>Før en krise bør du forberede deg.</p>
          <p>Det kan være lurt å finne møtesteder, fylle opp beredskapslageret og sjekke nærområdet.</p>
        </div>

        <div class="box">
          <h3>UNDER</h3>
          <p>Under en krise er det viktig å holde seg oppdatert.</p>
          <p>Følg med på nyheter og lokale varsler.</p>
        </div>

        <div class="box">
          <h3>ETTER</h3>
          <p>Etter en krise bør du evaluere og lære.</p>
          <p>Følg råd fra myndigheter og noter erfaringer.</p>
        </div>
      </div>

      <div class="info-header">
        <p>Nedenfor finner du en oversikt over de ulike tjenestene vi tilbyr.</p>
      </div>

      <div class="grid-container">
        <router-link to="/map" class="box">
          <h3>Finn tilfluktsrom</h3>
          <p>Se oversikt over tilfluktsrom i nærheten.</p>
        </router-link>

        <router-link to="/map" class="box">
          <h3>Møtesteder</h3>
          <p>Se hvor du kan møte andre ved krise.</p>
        </router-link>

        <div class="box unavailable" data-tooltip="Tjenesten er ikke tilgjengelig enda.">
          <h3>Nyheter</h3>
          <p>Hold deg oppdatert på relevante hendelser.</p>
        </div>

        <router-link to="/login" class="box unavailable" data-tooltip="Logg inn for å få tilgang.">
          <h3>Husstand</h3>
          <p>Se oversikt over medlemmene i din husstand.</p>
        </router-link>

        <router-link to="/login" class="box unavailable" data-tooltip="Logg inn for å få tilgang.">
          <h3>Beredskapslager</h3>
          <p>Sjekk om du har nok mat og drikke.</p>
        </router-link>

        <router-link to="/login" class="box unavailable" data-tooltip="Logg inn for å få tilgang.">
          <h3>Erfaringer</h3>
          <p>Skriv et notat fra en krise du har opplevd.</p>
        </router-link>
      </div>
    </main>

    <Footer />
  </div>
</template>

<script lang="ts" setup>
import HeaderBase from '@/components/HeaderBase.vue'
import Footer from '@/components/Footer.vue'
</script>
