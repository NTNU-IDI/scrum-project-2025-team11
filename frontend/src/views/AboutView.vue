<template>
  <div class="page-wrapper">
    <Header />

    <main class="main-content">
      <div class="info-header">
        <h1>Om oss</h1>
        <p>Nedenfor finner du utviklerne bak siden.</p>
      </div>

      <div class="grid-container">
        <div class="box">
          <h3>An</h3>
          <p>Om person 1</p>
        </div>

        <div class="box">
          <h3>August</h3>
          <p>Om person 2</p>
        </div>

        <div class="box">
          <h3>Gabriel</h3>
          <p>Om person 3</p>
        </div>

        <div class="box">
          <h3>Garv</h3>
          <p>Om person 4</p>
        </div>

        <div class="box">
          <h3>Jonas</h3>
          <p>Om person 5</p>
        </div>

        <div class="box">
          <h3>Madeleine</h3>
          <p>Om person 6</p>
        </div>

        <div class="box">
          <h3>Matteus</h3>
          <p>Om person 7</p>
        </div>

        <div class="box">
          <h3>Therese</h3>
          <p>Om person 8</p>
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
