import { shallowMount, mount } from '@vue/test-utils';
import ViewMembersComponent from '@/components/household/ViewMembersComponent.vue';
import { useUserStore } from '@/stores/userStore';
import { useHouseholdStore } from '@/stores/householdStore';
import { createPinia, setActivePinia } from 'pinia';
import { beforeEach, describe, expect, it } from 'vitest';

describe('ViewMembersComponent', () => {
    // Set up Pinia store for testing
    let userStore: any;
    let householdStore: any;

    beforeEach(() => {
        // Create a new Pinia instance and set it as active for the test
        const pinia = createPinia();
        setActivePinia(pinia);
        
        userStore = useUserStore();
        householdStore = useHouseholdStore();
    });

    // Checks that the component renders the correct household name
    it('renders household name correctly after login', async () => {
        await householdStore.fetchHousehold();

        const wrapper = shallowMount(ViewMembersComponent, {
        global: {
            plugins: [setActivePinia(createPinia())], 
        },
        });

        await wrapper.vm.$nextTick();

        expect(wrapper.text()).toContain(householdStore.name);
    });

    // Checks that the component renders the correct number of members
    /*
    it('renders correct member count', async () => {
        householdStore.setHousehold({ id: 1, name: 'Familien Larsen', memberCount: 5, addressId: '1' });

        const wrapper = shallowMount(ViewMembersComponent, {
        global: {
            plugins: [createPinia()],
        },
        });

        const membersInput = wrapper.find('#members-input').element as HTMLInputElement;
        expect(membersInput.value).toBe('5');
    });
    */

    // Checks that the component rendes the correct section titles    
    it('renders the correct section titles', async () => {
        const wrapper = shallowMount(ViewMembersComponent, {
        global: {
            plugins: [createPinia()],
        },
        });

        const headers = wrapper.findAll('h1');
        const headerTexts = headers.map(h => h.text());

        expect(headerTexts).toContain('Deg');
        expect(headerTexts).toContain('Antall medlemmer');
    });

    // Checks that the component renders the correct button response
    it('emits "show-new-member-box" event when button is clicked', async () => {
        const wrapper = mount(ViewMembersComponent, {
        global: {
            plugins: [createPinia()],
        },
        });

        const button = wrapper.find('#invite-button');
        await button.trigger('click');

        expect(wrapper.emitted()).toHaveProperty('show-new-member-box');
    });

});
