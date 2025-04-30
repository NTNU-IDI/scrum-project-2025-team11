<template>
    <div class="home">
      <Header />
    
      <main class="main-content">
        <div class="info-header">
          <h1>Velkommen til Krisefikseren!</h1>
        </div>
    
        <div clasS="info-header">
          <p>Vet du hva du bør gjøre FØR, UNDER og ETTER en krise?</p>
        </div>
    
        <div class="grid-container">
          <router-link to="/beredskap" class="box">
            <h3>FØR</h3>
            <p>Før en krise bør du forberede deg.</p>
            <p>Da kan det være lurt å finne lokale møtesteder, fylle opp beredskapslageret ditt og sjekke nærområdet ditt.</p>
          </router-link>
    
          <router-link to="/beredskap" class="box">
            <h3>UNDER</h3>
            <p>Under en krise er det viktig å holde seg oppdatert.</p>
            <p>Da kan det være lurt å følge med på nyheter og lokale varsler.</p>
          </router-link>
          <router-link to="/beredskap" class="box">
            <h3>ETTER</h3>
            <p>Etter en krise er det viktig å vite hva du skal gjøre.</p>
            <p>Da kan det være lurt å sjekke med lokale myndigheter og følge med på nyheter, og kanskje notere hva du lærte for</p>
            <p>å være bedre forberedt neste gang.</p>
          </router-link>
        </div>
    
        <div class="info-header">
          <p>Nedenfor finner du en oversikt over de ulike tjenestene vi kan tilby.</p>
        </div>
    
        <div class="grid-container">
          <router-link to="/map" class="box">
            <h3>Person 1</h3>
            <p>om person 1</p>
          </router-link>
    
          <router-link to="/map" class="box">
            <h3>Person 2</h3>
            <p>om person 2</p>
          </router-link>

          <router-link to="/map" class="box">
            <h3>Person 3</h3>
            <p>om person 3</p>
          </router-link>

          <router-link to="/map" class="box">
            <h3>Person 4</h3>
            <p>om person 4</p>
          </router-link>

          <router-link to="/map" class="box">
            <h3>Person 5</h3>
            <p>om person 5</p>
          </router-link>

          <router-link to="/map" class="box">
            <h3>Person 6</h3>
            <p>om person 6</p>
          </router-link>

          <router-link to="/map" class="box">
            <h3>Person 7</h3>
            <p>om person 7</p>
          </router-link>

          <router-link to="/map" class="box">
            <h3>Person 8</h3>
            <p>om person 8</p>
          </router-link>
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
    .home {
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
      padding: 6px;
      border-radius: 8px;
      margin: 10px 125px;
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
      margin: 40px 100px;
    }
    
    .box {
      background-color: var(--light-blue);
      border: 2px solid var(--light-blue);
      color: white;
      padding: 24px;
      border-radius: 8px;
      transition: background-color 0.4s ease;
      box-shadow: 0 4px 6px rgba(0.5, 0, 0, 0.5);
      text-decoration: none;
      display: block;
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
    