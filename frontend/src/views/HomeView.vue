<template>
<div class="flex flex-col min-h-screen">
    <Header />

    <main class="flex-1 px-8 py-6 bg-white">
      <div class="info-header">
        <h1 class="text-4xl font-bold mb-4">Velkommen til Krisefikseren!</h1>
        <p class="text-3xl font-bold mb-2">Nedenfor finner du en oversikt over de ulikene tjenestene vi kan tilby.</p>
      </div>

      <div class="grid-container">
        <div>
          <h3 class="text-xl font-semibold mb-2">Finn tilfluktsrom</h3>
          <p>Se oversikt over tilfluktsrom i nærheten.</p>
        </div>
        <div>
          <h3 class="text-xl font-semibold mb-2">Beredskapsråd</h3>
          <p>Lær hvordan du forbereder deg på kriser.</p>
        </div>
        <div>
          <h3 class="text-xl font-semibold mb-2">Nyheter</h3>
          <p>Hold deg oppdatert på relevante hendelser.</p>
        </div>
        <div>
          <h3 class="text-xl font-semibold mb-2">Tjenestekart</h3>
          <p>Se hvor du finner hjelp i ditt område.</p>
        </div>
        <div>
          <h3 class="text-xl font-semibold mb-2">Varslingssystem</h3>
          <p>Få varsler ved kritiske hendelser i nærheten.</p>
        </div>
        <div>
          <h3 class="text-xl font-semibold mb-2">Kontakt</h3>
          <p>Kom i kontakt med vår beredskapsgruppe.</p>
        </div>
      </div>
    </main>
    <Footer />
  </div>
</template>

<script lang="ts" setup>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
</script>


<style scoped>
.info-header {
  background-color: #ffffff;
  color: var(--darkest-blue);
  padding: 24px;
  border-radius: 8px;
  margin: 40px 125px;
  
}
.info-header h1 {
  margin: 0;
  font-size: 2.5rem;
}
.info-header h2 {
  margin: 0;
  font-size: 2rem;
}
.info-header p {
  margin: 0;
  font-size: 1.25rem;
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

.grid-container > div {
  background-color: var(--light-blue);
  border-color: var(--light-blue);
  color: white;
  padding: 24px;
  border-radius: 8px;
  transition: background-color 0.4s ease;
  box-shadow: 0 4px 6px rgba(0.5, 0, 0, 0.5);
  
}
.grid-container > div h3 {
  margin: 0;
  font-size: 1.5rem;
}
.grid-container > div p {
  margin: 0;
  font-size: 1rem;
}

.grid-container > div:hover {
  background-color: rgb(169, 184, 203);
  border-color: var(--light-blue);
  color: var(--darkest-blue);
  cursor: pointer;
}

</style>


