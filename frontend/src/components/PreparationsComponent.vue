<script setup lang="ts">
import { computed } from 'vue'
import { defineProps } from 'vue'

const props = defineProps({
  stage: {
    type: String,
    required: true
  }
})

const title = computed(() => {
  switch (props.stage) {
    case 'før':
      return 'Før en krise intreffer'
    case 'under':
      return 'Under en krise'
    case 'etter':
      return 'Etter en krise'
    default:
      return 'Hvem vet?'
  }
})

const message = computed(() => {
  switch (props.stage) {
    case 'før':
      return 'Vi lever i en stadig mer urolig verden - blant annet som følge av klimaendringer, krig og digitale trusler. Selv om det meste fungerer som det skal i Norge, må vi være forberedt på at ekstremvær, pandemier, ulykker, sabotasje og i verste fall krigshandlinger kan ramme oss.'
    case 'under':
      return 'Krisesituasjoner er uoversiktlige, og informasjonen du får påvirker valgene du tar. I en krise bør du søke informasjon fra myndighetenes kanaler og fra redaktørstyrte journalistiske medier som er underlagt Vær Varsom-plakaten og redaktørplakaten. NRK P1 er beredskapskanalen som skal sikre informasjon til publikum, også dersom andre nyhetsmedier og offentlige nettsteder ikke er tilgjengelige.  '
    case 'etter':
      return 'vibeeeeeeeee'
    default:
      return 'Vennligst velg et riktig sikkerhets stadiet'
  }
})

const listHeader = computed(() => {
    switch (props.stage) {
        case 'før':
            return 'Sjekkliste for egen beredskap'
        case 'under': 
            return 'Praktiske råd hvis internett eller mobilnett ikke fungerer'
    }
})

const list = computed(() => {
    switch (props.stage) {
        case 'før':
            return [
            'Rent drikkevann lagret på kanner eller flasker. 20 liter per person',
            'Mat som tåler lagring i romtemperatur.',
            'Grill, kokeapparat eller stormkjøkken.',
            'Varme klær, pledd, dyner og soveposer.',
            'Fyrstikker og stearinlys.',
            'Ved hvis du har vedovn eller peis.',
            'Gass- eller parafinovn som er beregnet for innendørs bruk er et alternativ til vedfyring.',
            'Avtale om overnatting hvis du ikke har alternativ oppvarming.',
            'Lommelykter eller hodelykter som går på batterier, sveiv eller solceller.',
            'DAB-radio som går på batterier, sveiv eller solceller.',
            'Legemidler og førstehjelpsutstyr.',
            'Jodtabletter (gjelder barn og voksne under 40 år, gravide og ammende).',
            'Hygieneartikler som våtservietter, håndsprit, bleier, dopapir og menstruasjonsprodukter.',
            'Batterier og ladet batteribank.',
            'Litt kontanter og flere betalingskort.',
            'Mat og vann til kjæledyr',
            'Liste på papir med viktige telefonnummer som for eksempel nødnummer, legevakt, veterinær, familie, venner og naboer.'
            ]
        case 'under':
            return [
                'Hvis du har bil kan du både høre på radio og lade mobilen i bilen, så lenge den har strøm eller drivstoff.',
                'Sjekk om du kan aktivere tale over wifi på din telefon. Da kan du ringe selv om mobilnettet er nede – så lenge du har tilgang til internett. Noen apper på telefonen kan også kommunisere over internett selv om mobilnettet er nede.',
                'Sjekk om familie eller nabo har abonnement tilknyttet et annet mobilnett enn deg. Da kan dere hjelpe hverandre dersom kun ett av nettene er nede.'
            ]
    }
})
</script>


<template>
    <div class="grey-container">
        <h1>{{ title }}</h1>
        <div>{{ message }}</div>
        <h2>{{ listHeader }}</h2>
        <li v-for="(item, index) in list" :key="index">
            {{item}}
        </li>
    </div>
</template>

<style scoped>
    .grey-container {
        width: 450px;
    }
</style>