<template>


  <header class="bg-blue-600 text-white p-4">
    <div class="container mx-auto flex justify-between items-center">
      <h1 class="text-2xl font-bold">Krisefikser.no</h1>
      <nav class="space-x-4">
        <a href="/" class="hover:underline">Hjem</a>
        <a href="/login" class="hover:underline">Logg inn</a>
      </nav>
    </div>
  </header>

  <button>Default Button</button>
<a href="#" class="button">Link Button</a>
<button class="button">Button</button>
<input type="button" class="button" value="Input Button">

</template>

<script lang="ts" setup>

</script>

<style>
.button {
  background-color: #04AA6D;
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
}
</style>
