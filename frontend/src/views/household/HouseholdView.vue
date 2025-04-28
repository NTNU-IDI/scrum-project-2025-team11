<script lang="ts" setup>
import NewItemComponent from '@/components/household/NewItemComponent.vue';
import ViewSingleItemComponent from '@/components/household/ViewSingleItemComponent.vue';
import ViewSuppliesComponent from '@/components/household/ViewSuppliesComponent.vue';
import { ref } from 'vue';

// Toggle new item component visibility
const isBoxVisible = ref(false);

const toggleNewItemBox = () => {
	isBoxVisible.value = !isBoxVisible.value;
}
</script>

<template>
  	<div class="page-container">
    	<div class="column">
      		<ViewSuppliesComponent @show-new-item-box="toggleNewItemBox" @hide-new-item-box="isBoxVisible = false" />
    	</div>
    	<div class="column">
      		<ViewSingleItemComponent />
    	</div>
		<div class="new-item-box">
			<NewItemComponent v-if="isBoxVisible" @hide-new-item-box="toggleNewItemBox"/>
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
	.new-item-box {
		position: absolute;
		bottom: 24.5rem;
		right: 8.5rem;
	}
</style>