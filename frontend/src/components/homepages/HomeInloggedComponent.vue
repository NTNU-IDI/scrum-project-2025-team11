<template>  
    <main class="main-content">
      <div class="info-header">
        <h1>Min beredskap</h1>
        <p>Her kan du sjekke din beredskap.</p>
      </div>

      <div class="grid-container">
        <router-link to="/household" class="box registered">
          <h3>Husstand og Beredskapslager</h3>
          <p>Se oversikt over medlemmene i husstanden din, og sjekk om du har nok forsyninger til en krise.</p>
        </router-link>

      </div>

      <div class="grid-container">
        <div class="box info">
          <h3>FØR</h3>
          <p>Før en krise bør du forberede deg.</p>
          <p>Det kan være lurt å finne møtesteder, fylle opp beredskapslageret og sjekke nærområdet.</p>
        </div>

        <div class="box info">
          <h3>UNDER</h3>
          <p>Under en krise er det viktig å holde seg oppdatert.</p>
          <p>Følg med på nyheter og lokale varsler.</p>
        </div>

        <div class="box info">
          <h3>ETTER</h3>
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
          <p>Her er ulike tjenester du kan finne på kartet:</p>
          <br><p><i class="fa fa-check-square"></i> Finne helsetjenester og medisinsk hjelp.</p>
          <br><p><i class="fa fa-check-square"></i> Finne møtesteder under en krise.</p>
          <br><p><i class="fa fa-check-square"></i> Finne tilfluktsrom i nærheten.</p>
          <br><a href="/map" class="button login"><i class="fa fa-map"></i> Til kartet</a> 
        </div>

        <div class="box info">
          <h3>Husstand og Beredskapslager</h3>
          <p>Her er ulike tjenester du kan finne om Husstand og Beredskapslager:</p>
          <br><p><i class="fa fa-check-square"></i> Se oversikt over medlemmene i husstanden din.</p>
          <br><p><i class="fa fa-check-square"></i> Sjekk om du har nok forsyninger til en krise.</p>
          <br><p><i class="fa fa-check-square"></i> Invitere andre inn i husstanden din.</p>
          <br><a href="/household" class="button login"><i class="fa fa-gear"></i> Til Husstand og Beredskapslager</a> 
        </div>

      </div>
    </main>
</template>