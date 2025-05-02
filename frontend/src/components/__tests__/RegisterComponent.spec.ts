import {describe, it, expect, beforeEach} from 'vitest'
import {nextTick} from "vue"

import { mount } from '@vue/test-utils'
import RegisterComponent from '../RegisterComponent.vue'

const EMPTY_STRING = ""
const FIRST_NAME_V = "Jan Erik"
const FIRST_NAME_IV_NO = "Navn123"
const LAST_NAME_V = "Navnesen-Hansen"
const LAST_NAME_IV_NO = "Navnesen123"
const USERNAME_V = "Jan123"
const USERNAME_IV_SPACE = "Jan NavnHans"
const USERNAME_IV_SPECIAL_CHARACTER = "Jan123!"
const EMAIL_V = "janerik@mail.no"
const EMAIL_IV = "janerik@no"
const PASSWORD_V = "passord123"
const REPEATED_PASSWORD_V = PASSWORD_V
//const HOUSHOLDCODE_V = "???"
const HOUSEHOLDNAME_V = "Familien Navnesen-Hansen"
const HOUSEHOLDNAME_IV_NO = "Familien 123"
const ADDRESS_V = "Trondheimsfjorden 123"
const POSTALCODE_V = "1234"

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
        expect(wrapper.find("#iptHouseholdCode").exists()).toBeTruthy()
        expect(wrapper.find("#divPlaceInfo").exists()).toBeFalsy()

        //Test that address fields are shown when the second radio is triggered
        radioInput[1].trigger("change")
        await nextTick()

        expect(wrapper.find("#iptHouseholdCode").exists()).toBeFalsy()
        expect(wrapper.find("#divPlaceInfo").exists()).toBeTruthy()
        if (wrapper.find("#divPlaceInfo").exists()) {
            expect(wrapper.find("#divPlaceInfo").findAll("input").length).toBe(2)
        }

        //Test that register-button shows
        const registerButton = wrapper.find("button")
        expect(registerButton.exists()).toBeTruthy()
        expect(registerButton.text()).toBe("Registrer")

        //Test that link shows and leads to login
        const linkToLogin = wrapper.find(".link")
        expect(linkToLogin.exists()).toBeTruthy()
        expect(linkToLogin.attributes("href")).toBe("/login")
        expect(linkToLogin.text()).toBe("Logg inn her")
    })
    describe(' in case of ', () => {
        const wrapper = mount(RegisterComponent)

        //Kan flytte de første feltene før wrapper.findAll til å være på utsida av de to describsa for new eller existing household
        const errorMessage = wrapper.find("#error")
        const iptFirstName = wrapper.find("#iptFirstName")
        const iptLastName = wrapper.find("#iptLastName")
        const iptUsername = wrapper.find("#iptUsername")
        const iptEmail = wrapper.find("#iptEmail")
        const iptPassword = wrapper.find("#iptPassword")
        const iptRepeatedPassword = wrapper.find("#iptRepeatedPassword")
        const cbPrivacyPolicy = wrapper.find("#cbPrivacyPolicy")

        describe(' new household chosen ', () => {

            wrapper.findAll("input[type='radio']")[0].trigger("change")
            const iptHouseholdName = wrapper.find("#iptHouseholdName")
            const iptAddress = wrapper.find("#iptAddress")
            const iptPostalCode = wrapper.find("#iptPostalCode")

            beforeEach(() => {
                iptFirstName.setValue(FIRST_NAME_V)
                iptLastName.setValue(LAST_NAME_V)
                iptUsername.setValue(USERNAME_V)
                iptEmail.setValue(EMAIL_V)
                iptPassword.setValue(PASSWORD_V)
                iptRepeatedPassword.setValue(REPEATED_PASSWORD_V)
                iptHouseholdName.setValue(HOUSEHOLDNAME_V)
                iptAddress.setValue(ADDRESS_V)
                iptPostalCode.setValue(POSTALCODE_V)
                cbPrivacyPolicy.setValue(true)
            })

            it('with valid input, no errormessage is shown', () => {
                expect(wrapper.vm.validateFields()).toBeTruthy()
                expect((iptFirstName.element as HTMLInputElement).value).toBe(FIRST_NAME_V)
                expect((iptLastName.element as HTMLInputElement).value).toBe(LAST_NAME_V)
                expect((iptUsername.element as HTMLInputElement).value).toBe(USERNAME_V)
                expect((iptPassword.element as HTMLInputElement).value).toBe(PASSWORD_V)
                expect((iptRepeatedPassword.element as HTMLInputElement).value).toBe(PASSWORD_V)
                expect((iptHouseholdName.element as HTMLInputElement).value).toBe(HOUSEHOLDNAME_V)
                expect((iptAddress.element as HTMLInputElement).value).toBe(ADDRESS_V)
                expect((iptPostalCode.element as HTMLInputElement).value).toBe(POSTALCODE_V)
                expect((cbPrivacyPolicy.element as HTMLInputElement).checked).toBe(true)

                expect(errorMessage.text()).toBe(EMPTY_STRING)
            })
            it('with invalid first name, errormessage is shown', async () => {
                await iptFirstName.setValue(EMPTY_STRING)
                //Got help from ChatGPT with the use of 'iptFirstName.element as HTMLInputElement).value' to get the value of an element
                expect((iptFirstName.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)

                await iptFirstName.setValue(FIRST_NAME_IV_NO)
                expect((iptFirstName.element as HTMLInputElement).value).toBe(FIRST_NAME_IV_NO)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid last name, errormessage is shown', async () => {
                await iptLastName.setValue(EMPTY_STRING)
                expect((iptLastName.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)

                await iptLastName.setValue(LAST_NAME_IV_NO)
                expect((iptLastName.element as HTMLInputElement).value).toBe(LAST_NAME_IV_NO)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid username, errormessage is shown', async () => {
                await iptUsername.setValue(EMPTY_STRING)
                expect((iptUsername.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)

                await iptUsername.setValue(USERNAME_IV_SPACE)
                expect((iptUsername.element as HTMLInputElement).value).toBe(USERNAME_IV_SPACE)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)

                await iptUsername.setValue(USERNAME_IV_SPECIAL_CHARACTER)
                expect((iptUsername.element as HTMLInputElement).value).toBe(USERNAME_IV_SPECIAL_CHARACTER)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid email, errormessage is shown', async () => {
                await iptEmail.setValue(EMPTY_STRING)
                expect((iptEmail.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)

                await iptEmail.setValue(EMAIL_IV)
                expect((iptEmail.element as HTMLInputElement).value).toBe(EMAIL_IV)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid password, errormessage is shown', async () => {
                await iptPassword.setValue(EMPTY_STRING)
                expect((iptPassword.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid repeated password, errormessage is shown', async () => {
                await iptRepeatedPassword.setValue(EMPTY_STRING)
                expect((iptRepeatedPassword.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid household-name, errormessage is shown', async () => {
                await iptHouseholdName.setValue(EMPTY_STRING)
                expect((iptHouseholdName.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)

                await iptHouseholdName.setValue(HOUSEHOLDNAME_IV_NO)
                expect((iptHouseholdName.element as HTMLInputElement).value).toBe(HOUSEHOLDNAME_IV_NO)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid address, errormessage is shown', async () => {
                await iptAddress.setValue(EMPTY_STRING)
                expect((iptAddress.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with invalid postal code, errormessage is shown', async () => {
                await iptPostalCode.setValue(EMPTY_STRING)
                expect((iptPostalCode.element as HTMLInputElement).value).toBe(EMPTY_STRING)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
            it('with unchecked privacy policy, errormessage is shown', async () => {
                await cbPrivacyPolicy.setValue(false)
                expect((cbPrivacyPolicy.element as HTMLInputElement).checked).toBe(false)
                expect(wrapper.vm.validateFields()).toBeFalsy()
                await nextTick()

                expect(errorMessage.text()).not.toBe(EMPTY_STRING)
            })
        })
        //TODO: Add tests for when existing household-code is implemented
        /*describe(' existing household chosen ', () => {
            wrapper.findAll("input[type='radio']")[1].trigger("change")
            const iptHouseholdCode = wrapper.find("#iptHouseholdCode")

            beforeEach(() => {
                iptFirstName.setValue(FIRST_NAME_V)
                iptLastName.setValue(LAST_NAME_V)
                iptUsername.setValue(USERNAME_V)
                iptEmail.setValue(EMAIL_V)
                iptPassword.setValue(PASSWORD_V)
                iptRepeatedPassword.setValue(REPEATED_PASSWORD_V)
                cbPrivacyPolicy.setValue(true)
            })
        })*/
    })
})
