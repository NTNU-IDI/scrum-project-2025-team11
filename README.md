[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/9pMJKrGa)

# Krisefikser.no - IDATT2106 Systemutvikling 2 Vår 2025 Prosjekt

[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.4.4-blue.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5.13-green.svg)](https://vuejs.org)

Dette prosjektet består av det endelige resultatet til scrum-team 11 i IDATT2106 - Systemutvikling 2, våren 2025. Applikasjonen er laget for å forberede og hjelpe befolkningen gjennom en eventuell krisesituasjon. I applikasjonen har brukeren mulighet til å lese om nyttig informasjon, se hendelser og punkter i kartet, lage sin egen husstand og spesifisere sitt eget beredskapslager.

---
# Innholdsfortegnelse
- [Krisefikser.no - IDATT2106 Systemutvikling 2 Spring 2025 Project](#krisefikserno---idatt2106-systemutvikling-2-vår-2025-prosjekt)
    - [Klone prosjektet](#klone-prosjektet)
    - [Forutsetninger](#forutsetninger)
    - [Kjøre prosjektet](#kjøre-prosjektet)
      - [Installere npm](#installere-npm)
      - [Kjøre frontend](#kjøre-vue-frontend)
      - [Kjøre backend](#kjøre-backend)
    - [Systemets arkitektur og verktøy](#systemets-arkitektur-og-verktøy)
       - [Backend](#backend)
       - [Frontend](#frontend)
    - [Testing](#testing)
      - [Testdekning - backend](#se-testdekning-for-backend)
      - [Testkjøring - frontend](#kjøre-frontend-tester)
      - [Testdekning - frontend](#se-testdekning-for-frontend)

# Klone prosjektet
```
https://github.com/NTNU-IDI/scrum-project-2025-team11.git
```
# Forutsetninger
Før prosjektet kjøres er det viktig å ha følgende lastet ned på pcen:
- Java 21
- Vue
- Maven
- NPM

# Kjøre prosjektet
### Installere npm
```
cd frontend
npm install
```
### Kjøre vue frontend (gitt man ikke er i frontend-mappen)
```
cd frontend
npm run dev
```
### Kjøre backend
```
cd backend
mvn spring-boot:run
```
# Systemets arkitektur og verktøy
- ## Backend
    - **Språk:** Java 21
    - **Rammeverk:** Spring boot 3.4.4
    - **Sikkerhet:** JWT
    - **Database:** H2 filbasert
    - **API:** REST
    - **ORM:** JPA
    - **Build tool:** Maven
- ## Frontend
    - **Rammeverk:** Vue 3.5.13
    - **Build tool:** Vite
    - **Ruting:** Vue router
    - **State:** Pinia, Pinia-plugin-persistedstate
    - **Eksterne biblioteker:**
        - axios
        - leaflet
        - leaflet-routing-machine
        - vue-recaptcha-v3
        - vue-toast-notification
# Testing
### Se testdekning for backend
```
cd backend
mvn clean verify
```
### Kjøre frontend tester
```
cd frontend
npx vitest
```
### Se testdekning for frontend
```
cd frontend
npm run coverage
```
