<script lang="ts" setup>
import NewItemComponent from '@/components/household/NewItemComponent.vue';
import NewMemberComponent from '@/components/household/NewMemberComponent.vue';
import ViewMembersComponent from '@/components/household/ViewMembersComponent.vue';
import ViewSingleItemComponent from '@/components/household/ViewSingleItemComponent.vue';
import ViewSuppliesComponent from '@/components/household/ViewSuppliesComponent.vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { ref } from 'vue';
import { useToast } from 'vue-toast-notification';

// Toggle new item component visibility
const isItemBoxVisible = ref(false);
// Toggle new member component visibility
const isMemberBoxVisible = ref(false);

/**
 * Toast instance
 * @property {import('vue-toast-notification').Toast}
 */
 const $toast = useToast();

const toggleNewItemBox = () => {
	isItemBoxVisible.value = !isItemBoxVisible.value;
  if(isItemBoxVisible.value) {
    isMemberBoxVisible.value = false;
  }
}

const toggleNewMemberBox = () => {
	isMemberBoxVisible.value = !isMemberBoxVisible.value;
  if(isMemberBoxVisible.value) {
    isItemBoxVisible.value = false;
  }
}

const newItemSuccess = () => {
	isItemBoxVisible.value = false;
    $toast.success(`Vare er lagt til i lageret`, {
        duration: 3000,
        position: 'top-right'
    });
}
</script>

<template>
	<Header />
  	<div class="page-container">
		<h1>Min husstand</h1>
		<div class="members-container">
			<ViewMembersComponent @show-new-member-box="toggleNewMemberBox" @hide-new-member-box="isMemberBoxVisible = false"/>
		
			<div class="modal-overlay" v-if="isMemberBoxVisible" @click.self="isMemberBoxVisible = false">
				<NewMemberComponent
				@close="isMemberBoxVisible = false"
				@hide-new-member-box="isMemberBoxVisible = false"
				/>
			</div>
		</div>

		<div class="items-container">
			<div class="items-column">
			<ViewSuppliesComponent @show-new-item-box="toggleNewItemBox" @hide-new-item-box="isItemBoxVisible = false" />
			</div>
			<div class="items-column">
			<ViewSingleItemComponent />
			</div>
			<button class="dark-button" id="add-button" @click="toggleNewItemBox">+ Ny vare</button> 

			<div class="modal-overlay" v-if="isItemBoxVisible" @click.self="isItemBoxVisible = false">
				<NewItemComponent  
				@close="isItemBoxVisible = false"
				@hide-new-item-box="isItemBoxVisible = false" 
				@new-item-success="newItemSuccess"
				/>
			</div>
		</div>

		

  	</div>
	<Footer />
</template>

<style scoped>
	.page-container {
		display: flex;
		flex-direction: column;
		gap: 2rem;
		padding: 1rem;
    	margin-right: 0px;
		margin: 0 3rem 0 3rem ;
	}

	h1 {
		font-size: var(--font-size-xxlarge);
		font-weight: normal;
	}

	.items-container{
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

	.members-container, .items-container{
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		border-radius: 12px;
	}
	
	#add-button {
		background-color: var(--orange);
        display: flex;
        align-items: center; 
        justify-content: center; 
        width: 6.5rem; 
        height: 3.5rem; 
        margin-left: 0px;
        margin-top: 4.75rem;
    }

  @media(max-width: 480px) {
    .page-container {
      display: block
    }
    .items-container {
      flex-direction: column !important;
      gap: 1rem !important;
    }
    #add-button {
      margin-right: 0px !important;
      margin-top: -4rem !important;
    }
    .new-item-box {
      margin-top: 0rem;
    }
  }

</style>