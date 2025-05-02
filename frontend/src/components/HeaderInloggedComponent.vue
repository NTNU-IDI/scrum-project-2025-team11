<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <img src="../assets/invertednew.png" alt="Logo" class="logo" @click="goToHome" />
      </div>

      <div class="header-right">
        <img src="../assets/ikon/house-line-fill.png" alt="Husstand" class="icon" @click="goToHousehold" />
        <img src="../assets/ikon/list-plus-fill.png" alt="Lager" class="icon" @click="goToStorage" />
        <a href="/" class="button logout" @click.prevent="goToLogout">Logg ut</a>
      </div>
    </div>
  </header>
</template>

