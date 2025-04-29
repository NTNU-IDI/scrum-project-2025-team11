<template>
  <div class="icons-box">
    <h1>Ikoner</h1>
    <ul>
      <li><span class="icon star"></span> Tilfluktsrom</li>
      <li><span class="icon meetup"></span> Møteplass</li>
      <li><span class="icon medical"></span> Medisinks hjelp</li>
      <li><span class="icon danger severe"></span> Alvorlig kriseberørt område</li>
      <li><span class="icon danger moderate"></span> Moderat kriseberørt område</li>
      <li><span class="icon danger mild"></span> Lett kriseberørt område</li>
    </ul>
  </div>
</template>

<script setup lang="ts">
</script>


<style scoped>
.icons-box {
  position: absolute;  
  top: 20px;          
  left: 20px;       
  background: var(--white);
  padding: 1rem 1.5rem 1rem 1rem;
  border-radius: 10px;
  font-size: var(--font-size-medium);
  z-index: 1; 
  display: flex;
  flex-direction: column;
}

.icons-box ul {
  list-style: none;
  padding: 0;
}

.icons-box li {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}

.icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  margin-right: 0.5rem;
}

.icon.star {
  background: url('/yellow-star-fill-border.svg') no-repeat center center / contain;
}

.icon.meetup {
  background: url('/lightest-blue-triangle-fill-border.svg') no-repeat center center / contain;
}

.icon.medical {
  background: url('/bad-red-medical-fill-border.svg') no-repeat center center / contain;
}

.icon.danger {
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: inline-block;
}

.icon.danger.severe {
  background-color: var(--bad-red-opacity);
  border: 1px solid var(--bad-red);
}

.icon.danger.moderate {
  background-color: var(--light-orange-opacity);
  border: 1px solid var(--light-orange);
}

.icon.danger.mild {
  background-color: var(--yellow-opacity);
  border: 1px solid var(--yellow);
}

@media (max-width: 768px) {
  .icons-box h1 {
    font-size: var(--font-size-xlarge);
    margin-bottom: 5px;
  }

  .icons-box li {
    font-size: var(--font-size-small);
  }
}
</style>