<script lang="ts" setup>
import NewItemComponent from '@/components/household/NewItemComponent.vue';
import NewMemberComponent from '@/components/household/NewMemberComponent.vue';
import ViewMembersComponent from '@/components/household/ViewMembersComponent.vue';
import ViewSingleItemComponent from '@/components/household/ViewSingleItemComponent.vue';
import ViewSuppliesComponent from '@/components/household/ViewSuppliesComponent.vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { ref } from 'vue';

// Toggle new item component visibility
const isItemBoxVisible = ref(false);
// Toggle new member component visibility
const isMemberBoxVisible = ref(false);

const toggleNewItemBox = () => {
	isItemBoxVisible.value = !isItemBoxVisible.value;
}

const toggleNewMemberBox = () => {
	isMemberBoxVisible.value = !isMemberBoxVisible.value;
}

//TODO: se på det med new item card i morra, er gap til høyre
</script>

<template>
	<Header />
  	<div class="page-container">
      <h1>Min husstand</h1>
      <div class="members-container">
        <ViewMembersComponent @show-new-member-box="toggleNewMemberBox" @hide-new-member-box="isMemberBoxVisible = false"/>
        </div>
      <div class="new-member-box">
        <NewMemberComponent v-if="isMemberBoxVisible" @hide-new-member-box="isMemberBoxVisible = false" />
      </div>

      <div class="items-container">
        <div class="items-column">
          <ViewSuppliesComponent @show-new-item-box="toggleNewItemBox" @hide-new-item-box="isItemBoxVisible = false" />
        </div>
        <div class="items-column">
          <ViewSingleItemComponent />
        </div>
        <button class="dark-button" id="add-button" @click="toggleNewItemBox">+</button>

        <div class="new-item-box">
          <NewItemComponent v-if="isItemBoxVisible" @hide-new-item-box="isItemBoxVisible = false"/>
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
	}

	h1 {
		font-size: var(--font-size-xxlarge);
		font-weight: normal;
	}

	.new-member-box { 
        align-self: center;
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

	#add-button {
        display: flex;
        align-items: center; 
        justify-content: center; 
        width: 3rem; 
        height: 3rem; 
        font-size: var(--font-size-xlarge);
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