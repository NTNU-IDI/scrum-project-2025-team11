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
        vi.spyOn(householdStore, 'setMemberCount');
        vi.spyOn(window, 'alert').mockImplementation(() => {});
        vi.clearAllMocks();
    });

    // Checks that the component renders the correct input fields and buttons
    it('renders email input and send button correctly', () => {
        const wrapper = shallowMount(NewMemberComponent, {
            global: {
                plugins: [createTestingPinia({ createSpy: vi.fn })],
            },
        });
    
        expect(wrapper.find('input[placeholder="E-mail"]').exists()).toBe(true);
        expect(wrapper.find('button.dark-button').exists()).toBe(true); 
        expect(wrapper.find('#invite-button').exists()).toBe(true); 
      });
    
    // Checks that the component renders an error message and no member is added if email is invalid 
    it('shows error and does not call addMember if first name is invalid', async () => {
        vi.spyOn(validationService, 'validateEmail').mockReturnValue(false);

        const wrapper = shallowMount(NewMemberComponent, {
        global: {
            plugins: [createPinia()],
        },
        });
    
        await wrapper.find('input').setValue('invalid-email');
        window.alert = vi.fn(); 
    
        const addButton = wrapper.findAll('button.dark-button')[0];
        await addButton.trigger('click');
    
        expect(window.alert).toHaveBeenCalledWith('Vennligst skriv inn en gyldig e-postadresse');
        expect(householdStore.setMemberCount).not.toHaveBeenCalled();
    });
    
    // Checks that new member box is hidden if email is valid
    it('emits "hide-new-member-box" if email is valid', async () => {
        vi.spyOn(validationService, 'validateEmail').mockReturnValue(true);
    
        const wrapper = shallowMount(NewMemberComponent, {
            global: {
                plugins: [createTestingPinia({ createSpy: vi.fn })],
            },
        });
    
        await wrapper.find('input').setValue('valid@email.com');
        await wrapper.find('#invite-button').trigger('click');
    
        expect(wrapper.emitted()).toHaveProperty('hide-new-member-box');
    });
    
    // Checks that new member box is hidden when cancel button is clicked
    it('emits "hide-new-member-box" when cancel button is clicked', async () => {
        const wrapper = shallowMount(NewMemberComponent, {
            global: {
            plugins: [createTestingPinia({ createSpy: vi.fn })],
            },
        });

        const cancelButton = wrapper.find('button.cancel-button');
        await cancelButton.trigger('click');

        expect(wrapper.emitted()).toHaveProperty('hide-new-member-box');
    });

   // TODO: Add test for invite button
});