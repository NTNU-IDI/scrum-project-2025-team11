import { shallowMount } from '@vue/test-utils';
import NewMemberComponent from '@/components/household/NewMemberComponent.vue';
import { useHouseholdStore } from '@/stores/householdStore';
import { createPinia, setActivePinia } from 'pinia';
import { createTestingPinia } from '@pinia/testing';
import { beforeEach, describe, expect, it, vi, type Mock } from 'vitest';
import * as validationService from '@/utils/validationService';

describe ('NewMemberComponent', () => {
    let householdStore: any;
    beforeEach(() => {
        const pinia = createPinia();
        setActivePinia(pinia);
        householdStore = useHouseholdStore();
        vi.spyOn(householdStore, 'addMember');
        vi.spyOn(window, 'alert').mockImplementation(() => {});
        vi.clearAllMocks();
    });

    // Checks that the component renders the correct input fields and buttons
    it('renders input fields and buttons correctly', () => {
        const wrapper = shallowMount(NewMemberComponent, {
            global: {
                plugins: [createPinia()],
            },
        });
    
        expect(wrapper.find('input[placeholder="Fornavn"]').exists()).toBe(true);
        expect(wrapper.find('input[placeholder="Etternavn"]').exists()).toBe(true);
        expect(wrapper.find('button.dark-button').exists()).toBe(true); 
        expect(wrapper.find('#invite-button').exists()).toBe(true); 
      });
    
    // Checks that the component renders an error message and no member is added if the first name is invalid 
    it('shows error and does not call addMember if first name is invalid', async () => {
        vi.spyOn(validationService, 'validateFirstName').mockReturnValue(false);
        vi.spyOn(validationService, 'validateLastName').mockReturnValue(true);

        const wrapper = shallowMount(NewMemberComponent, {
        global: {
            plugins: [createPinia()],
        },
        });
    
        await wrapper.find('input[placeholder="Fornavn"]').setValue('');
        await wrapper.find('input[placeholder="Etternavn"]').setValue('ValidLastName');
    
        window.alert = vi.fn(); 
    
        const addButton = wrapper.findAll('button.dark-button')[0];
        await addButton.trigger('click');
    
        expect(window.alert).toHaveBeenCalledWith('Vennligst skriv inn et gyldig fornavn');
        expect(householdStore.addMember).not.toHaveBeenCalled();
    });
    
    // Checks that the component renders an error message and no member is added if the last name is invalid 
    it('shows error and does not call addMember if last name is invalid', async () => {
        vi.spyOn(validationService, 'validateFirstName').mockReturnValue(true);
        vi.spyOn(validationService, 'validateLastName').mockReturnValue(false);

        const wrapper = shallowMount(NewMemberComponent, {
            global: {
            plugins: [createPinia()],
            },
        });

        await wrapper.find('input[placeholder="Fornavn"]').setValue('ValidFirstName');
        await wrapper.find('input[placeholder="Etternavn"]').setValue('');

        window.alert = vi.fn(); 

        const addButton = wrapper.findAll('button.dark-button')[0];
        await addButton.trigger('click');

        expect(window.alert).toHaveBeenCalledWith('Vennligst skriv inn et gyldig etternavn');
        expect(householdStore.addMember).not.toHaveBeenCalled();
    });
    
    // Checks that member is added and new member box is hidden if both names are valid
    it('calls addMember and emits "hide-new-member-box" if both names are valid', async () => {
        vi.spyOn(validationService, 'validateFirstName').mockReturnValue(true);
        vi.spyOn(validationService, 'validateLastName').mockReturnValue(true);

        const wrapper = shallowMount(NewMemberComponent, {
            global: {
            plugins: [createTestingPinia({
                createSpy: vi.fn, 
              }),],
            },
        });

        await wrapper.find('input[placeholder="Fornavn"]').setValue('ValidFirstName');
        await wrapper.find('input[placeholder="Etternavn"]').setValue('ValidLastName');

        const addButton = wrapper.findAll('button.dark-button')[0];
        await addButton.trigger('click');

        const householdStore = useHouseholdStore();
        expect(householdStore.addMember).toHaveBeenCalled();
        expect(wrapper.emitted()).toHaveProperty('hide-new-member-box');
    });
    
    // Checks that new member box is hidden when cancel button is clicked
    it('emits "hide-new-member-box" when cancel button is clicked', async () => {
        const wrapper = shallowMount(NewMemberComponent, {
            global: {
            plugins: [createPinia()],
            },
        });

        const cancelButton = wrapper.find('button.cancel-button');
        await cancelButton.trigger('click');

        expect(wrapper.emitted()).toHaveProperty('hide-new-member-box');
    });

   // TODO: Add test for invite button
});