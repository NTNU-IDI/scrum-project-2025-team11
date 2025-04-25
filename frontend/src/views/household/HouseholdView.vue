<script lang="ts" setup>
import ViewSingleArticleComponent from '@/components/household/ViewSingleArticleComponent.vue';
import ViewSuppliesComponent from '@/components/household/ViewSuppliesComponent.vue';

</script>

<template>
  	<div class="page-container">
    	<div class="column">
      		<ViewSuppliesComponent />
    	</div>
    	<div class="column">
      		<ViewSingleArticleComponent />
    	</div>
  	</div>
</template>

<style scoped>
	.page-container {
		display: flex;
		flex-direction: row;
		gap: 2rem;
		padding: 1rem;
	}
	.column {
		display: flex;
  		flex-direction: column;
  		gap: 1rem;
	}
</style>