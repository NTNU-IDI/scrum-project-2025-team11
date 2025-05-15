[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/9pMJKrGa)

# Krisefikser.no - IDATT2106 Systemutvikling 2 Spring 2025 Project

[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.4.4-blue.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5.13-green.svg)](https://vuejs.org)

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
Installere npm
```
npm install
```
Kjøre vue frontend
```
npm run dev
```
Kompilere backend
```
mvn clean install
```
Kjøre backend
```
mvn spring-boot:run
```
# Systemets arkitektur og verktøy
- Backend
    - Språk: Java 21
    - Rammeverk: Spring boot 3.4.4
    - Sikkerhet:
    - Database: H2 filbasert
    - API: REST
    - ORM: JPA
    - Build tool: Maven
    
- Frontend
    - Rammeverk: Vue 3.5.13
    - Build tool: Vite
    - Ruting: Vue router
    - State: Pinia, Pinia-plugin-persistedstate
    - Eksterne biblioteker:
        - axios
        - leaflet
        - leaflet-routing-machine
        - vue-recaptcha-v3
        - vue-toast-notification
# Testing
Se testcoverage for backend
```
cd backend
mvn clean verify
```
Kjøre frontend tester
```
cd frontend
npx vitest
```
Se testcoverage for frontend
```
cd frontend
npm run coverage
```
