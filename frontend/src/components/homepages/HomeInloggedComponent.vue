<template>  
  <main class="main-content">
    <div class="info-header">
      <h1>Velkommen til Krisefikser.no!</h1>
      <p>- Beredskap gjort enkelt - </p>
    </div>

    <PreparednessComponent />

    <div class="info-header">
      <h1>Våre tjenester</h1>
      <p>Nedenfor finner du en oversikt over de ulike tjenestene vi tilbyr.</p>
    </div>

    <div class="grid-container">
      
      <div class="box info">
        <h3>Om kartet</h3>
        <p>Her er ulike tjenester du kan finne på kartet</p>
        <br><p><i class="fa fa-check-square" aria-hidden="true"></i> Finne helsetjenester og medisinsk hjelp.</p>
        <br><p><i class="fa fa-check-square" aria-hidden="true"></i> Finne møtesteder under en krise.</p>
        <br><p><i class="fa fa-check-square" aria-hidden="true"></i> Finne tilfluktsrom i nærheten.</p>
        <router-link to="/map" class="button login"><i class="fa fa-map" aria-hidden="true"></i> Til kartet</router-link>
      </div>

      <div class="box info">
        <h3>Husstand og Beredskapslager</h3>
        <p>Her er ulike tjenester du kan finne om Husstand og Beredskapslager:</p>
        <br><p><i class="fa fa-check-square"></i> Se oversikt over medlemmene i husstanden din.</p>
        <br><p><i class="fa fa-check-square"></i> Sjekk om du har nok forsyninger til en krise.</p>
        <br><p><i class="fa fa-check-square"></i> Invitere andre inn i husstanden din.</p>
        <br><router-link to="/household" class="button login"><i class="fa fa-medkit" aria-hidden="true"></i> Til min beredskap</router-link>      
      </div>

    </div>
  </main>
</template>

<script lang="ts" setup>
import PreparednessComponent from '../PreparednessComponent.vue'
</script>