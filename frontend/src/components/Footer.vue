<template>
<footer class="p-4 bg-[var(--light-blue)] text-white">
    <!-- Her er footeren -->
    <br>
    <h1 class="text-l font-bold mb-2">Andre tjenester</h1>

    <!-- Her er to lenker til om oss og personvern rett under andre tjenester i mindre skrift-->
    <p class="text-sm">
        <a href="/about" class="text-white hover:text-gray-300">Om oss</a> |
        <a href="/privacy" class="text-white hover:text-gray-300">Personvern</a> |
        <a href="/userhome" class="text-white hover:text-gray-300">User home</a> |
        <a href="/admin" class="text-white hover:text-gray-300">Admin</a> |
        <button @click="openPDF" class="text-white hover:text-gray-300">Personvernsærklering</button>
        <br>
        <br>
        <br>
    </p>

  </footer>
</template>

<script>
export default {
    name: 'Footer'
}
/* Link to userhome and admin is added to footer  for testing purposes. */
import { useRouter } from 'vue-router'
const router = useRouter()
const goToHome = () => {
  router.push('/')
}
const goToUserHome = () => {
  router.push('/userhome')
}
const goToAdmin = () => {
  router.push('/admin')
}
const openPDF = () => {
  window.open('/path/to/your-file.pdf', '_blank');
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
/* Definerer fargen på lenkene i footeren og flytter dem til venstre*/
footer a {
  color: #ffffff;
  text-decoration: none;
  margin-left: 20px;
}


</style>