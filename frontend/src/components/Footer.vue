<template>
<footer class="p-4 bg-[var(--light-blue)] text-white text-center">
    <h1 class="text-l font-bold mb-2">Andre tjenesterRRRRRRRRRRRR</h1>
    <nav>
      <a href="/" class="mr-4 hover:underline">Hjem</a>
      <a href="/personvern" class="hover:underline">Personvern</a>
    </nav>
  </footer>
</template>

<script>
export default {
    name: 'Footer'
}
</script>

<style scoped>
/* Definerer bakrgunnen til footeren */
footer {
  background-color: #0d1b2a;
  color: white;
}
/* Definerer fargen på overskriften i footeren og flytter den til venstre*/
footer h1 {
  color: #ffffff;
  text-align: left;
  margin-left: 20px;
}

</style>