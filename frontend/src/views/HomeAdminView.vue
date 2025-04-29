<template>
    <div class="flex flex-col min-h-screen">
        <Header />
    
        <main class="flex-1 px-8 py-6 bg-white">
          <div class="info-header">
            <h1 class="text-4xl font-bold mb-4">Administrasjon av krisefikseren.no</h1>
            <p class="text-3xl font-bold mb-2">Husk at du gjennom dette grensesnittet har tilgang til krtiske opplysninger.</p>
            <p class="text-3xl font-bold mb-2">Vennligst ta ansvaret som følger med det.</p>
          </div>
    
          <div class="grid-container">
            <div>
              <h3 class="text-xl font-semibold mb-2">Krisehendelser</h3>
                <p>Her kan du adminsitrere opplysninger om hendelsene</p>
            </div>
            <div>
              <h3 class="text-xl font-semibold mb-2">Kartopplysninger</h3>
                <p>Her kan du adminsitrere opplysninger om kartet.</p>
            </div>
            <div>
              <h3 class="text-xl font-semibold mb-2">Adminbrukere</h3>
                <p>Her kan du adminsitrere opplysninger om adminbrukere.</p>
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
    
    
    