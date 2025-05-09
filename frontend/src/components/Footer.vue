<template>
  <footer class="footer">
    <div class="footer-content">
      <h2>Andre tjenester</h2>
      <nav class="footer-links" aria-label="Nyttige lenker">
        <router-link to="/personvern"><i class="fa fa-shield" aria-hidden="true"></i> Personvern</router-link>

        <!--
        <router-link to="/userhome">User home (for testing)</router-link>
        <router-link to="/admin">Admin (for testing)</router-link>
        <router-link to="/superadmin">Superadmin (for testing)</router-link>
        -->
        <br />
        <br />
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

@media (max-width: 768px) {
  .footer {
    text-align: center;
    padding: 32px 16px;
  }

  .footer-content h2 {
    font-size: 18px;
    text-align: center;
  }

  .footer-links {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    margin-top: 16px;
  }

  .footer-links a {
    margin: 0; /* Fjern horisontal margin */
    font-size: 16px;
  }
}


</style>
