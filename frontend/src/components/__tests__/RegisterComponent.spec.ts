import { describe, it, expect } from 'vitest'
import {nextTick} from "vue"

import { mount } from '@vue/test-utils'
import RegisterComponent from '../RegisterComponent.vue'

describe('RegisterComponent', () => {
    it('renders registration page properly', async () => {
        const wrapper = mount(RegisterComponent)

        await nextTick()

        //Test registration header
        expect(wrapper.text()).toContain('Registrer bruker')

        //Test constant fields load
        const constantInputDiv = wrapper.find("#divConstantInputs")
        const constantInputFields = constantInputDiv.findAll("input")
        expect(constantInputFields.length).toBe(6)

        //Test that two radio buttons are rendered
        const radioInput = wrapper.findAll("input[type='radio']")
        expect(radioInput.length).toBe(2)

        //Test that new household is the one that is rendered
        expect(wrapper.find("#divNewHouseholdInfo").exists()).toBeTruthy()
        expect(wrapper.find("#iptHouseholdCode").exists()).toBeFalsy()
        if (wrapper.find("#divNewHouseholdInfo").exists()) {
            //Check that there are three fields in total (Household name, address and postal code)
            expect(wrapper.find("#divNewHouseholdInfo").findAll("input").length).toBe(3)
        }

        //Test that address fields are shown when the second radio is triggered
        radioInput[1].trigger("change")
        await nextTick()

        expect(wrapper.find("#divNewHouseholdInfo").exists()).toBeFalsy()
        expect(wrapper.find("#iptHouseholdCode").exists()).toBeTruthy()

        //Test that checkbox for privacy policy is shown, links to privacy policy and opens a new page
        const cbPrivacyPolicy = wrapper.find("#cbPrivacyPolicy")
        expect(cbPrivacyPolicy.exists()).toBeTruthy()
        const linkToPrivacyPolicy = wrapper.find("#linkPrivacyPolicy")
        expect(linkToPrivacyPolicy.exists()).toBeTruthy()
        expect(linkToPrivacyPolicy.attributes("href")).toBe("/personvern")
        expect(linkToPrivacyPolicy.attributes("target")).toBe("_blank")

        //Test that register-button shows
        const registerButton = wrapper.find("button")
        expect(registerButton.exists()).toBeTruthy()
        expect(registerButton.text()).toBe("Registrer")

        //Test that link shows and leads to login
        const linkToLogin = wrapper.find(".register-login-text").find(".link")
        expect(linkToLogin.exists()).toBeTruthy()
        expect(linkToLogin.attributes("href")).toBe("/login")
        expect(linkToLogin.text()).toBe("Logg inn her")
    })
})
