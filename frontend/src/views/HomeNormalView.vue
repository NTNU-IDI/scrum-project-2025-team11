<template>
  <main class="main-content">
    <div class="info-header">
      <h1>Velkommen til Krisefikseren!</h1>
    </div>

    <div class="info-header">
      <p>Vet du hva du bør gjøre FØR, UNDER og ETTER en krise?</p>
    </div>

    <div class="grid-container">
      <div class="box info">
        <h3><i class="fa fa-arrow-left"> aria-hidden="true"</i> FØR</h3>
        <p>Før en krise bør du forberede deg.</p>
        <p>Det kan være lurt å finne møtesteder, fylle opp beredskapslageret og sjekke nærområdet.</p>
      </div>

      <div class="box info">
        <h3><i class="fa fa-warning"> aria-hidden="true"</i> UNDER</h3>
        <p>Under en krise er det viktig å holde seg oppdatert.</p>
        <p>Følg med på nyheter og lokale varsler.</p>
      </div>

      <div class="box info">
        <h3><i class="fa fa-arrow-right"> aria-hidden="true"</i> ETTER</h3>
        <p>Etter en krise bør du evaluere og lære.</p>
        <p>Følg råd fra myndigheter og noter erfaringer.</p>
      </div>
    </div>

    <div class="info-header">
      <h1>Våre tjenester</h1>
      <p>Nedenfor finner du en oversikt over de ulike tjenestene vi tilbyr.</p>
    </div>

    <div class="grid-container">
      
      <div class="box info">
        <h3>Om kartet</h3>
        <p>Her er ulike tjenester du kan finne på kartet</p>
        <br><p><i class="fa fa-check-square"> aria-hidden="true"</i> Finne helsetjenester og medisinsk hjelp.</p>
        <br><p><i class="fa fa-check-square"> aria-hidden="true"</i> Finne møtesteder under en krise.</p>
        <br><p><i class="fa fa-check-square"> aria-hidden="true"</i> Finne tilfluktsrom i nærheten.</p>
        <br><a href="/map" class="button login"><i class="fa fa-map" aria-hidden="true"></i>
          Til kartet</a> 
      </div>

      <router-link to="/login" class="box unavailable" data-tooltip="Logg inn for å få tilgang." aria-disabled="true" tabindex="-1">
        <h3>Husstand og Beredskapslager</h3>
        <p>Her er ulike tjenester du kan finne om Husstand og Beredskapslager</p>
        <br><p><i class="fa fa-check-square"> aria-hidden="true"</i> Se oversikt over medlemmene i husstanden din.</p>
        <br><p><i class="fa fa-check-square"> aria-hidden="true"</i> Sjekk om du har nok forsyninger til en krise.</p>
        <br><p><i class="fa fa-check-square"> aria-hidden="true"</i> Invitere andre inn i husstanden din.</p>
      </router-link>
    </div>
  </main>
</template>