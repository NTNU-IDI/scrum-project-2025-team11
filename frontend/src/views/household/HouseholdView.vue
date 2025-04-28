<script lang="ts" setup>
import NewItemComponent from '@/components/household/NewItemComponent.vue';
import ViewMembersComponent from '@/components/household/ViewMembersComponent.vue';
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
		<h1>Min husstand</h1>
		<div class="members-container">
			<ViewMembersComponent />
	  	</div>

		<div class="items-container">
			<div class="items-column">
				<ViewSuppliesComponent @show-new-item-box="toggleNewItemBox" @hide-new-item-box="isBoxVisible = false" />
			</div>
			<div class="items-column">
				<ViewSingleItemComponent />
			</div>
			<div class="new-item-box">
				<NewItemComponent v-if="isBoxVisible" @hide-new-item-box="toggleNewItemBox"/>
			</div>
		</div>
  	</div>
</template>

<style scoped>
	.page-container {
		display: flex;
		flex-direction: column;
		gap: 2rem;
		padding: 1rem;
	}
	.items-container {
		display: flex;
		flex-direction: row;
		gap: 2rem;
		padding: 1rem;
	}
	.items-column {
		display: flex;
  		flex-direction: column;
  		gap: 1rem;
	}
	.new-item-box {
		margin-top: 4.75rem;
	}
</style>