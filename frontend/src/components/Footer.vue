<template>
  <footer class="footer">
    <div class="footer-content">
      <h2>Andre tjenester</h2>
      <nav class="footer-links" aria-label="Nyttige lenker">
        <a href="/personvern"><i class="fa fa-lock" aria-hidden="true"></i> Personvern</a>
        <a href="/userhome">User home (for testing)</a>
        <a href="/admin">Admin (for testing)</a>
        <a href="/superadmin">Superadmin (for testing)</a>
        <br>
        <br>
      </nav>
    </div>
  </footer>
</template>

<script lang="ts" setup>

</script>

<style scoped>
.footer {
  background-color: var(--darkest-blue);
  color: white;
  padding: 20px;
}

/** The footer title  h1*/
.footer h1 {
  font-size: 32px;
  font-weight: bold;
  text-align: left;
  margin-bottom: 12px;
  color: white;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-content h1 {
  font-size: 20px;
  font-weight: bold;
  text-align: left;
  margin-bottom: 12px;
}

.footer-links {
  font-size: 14px;
  text-align: left;
  margin-top: 8px;
}
.footer-links:hover {
  cursor: pointer;
}

.footer-links a {
  color: white;
  text-decoration: none;
  margin-right: 12px;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #d1d5db;
}

.link-button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 14px;
  margin-left: 12px;
  transition: color 0.3s ease;
  padding: 0;
}

.link-button:hover {
  color: #d1d5db;
}

</style>
