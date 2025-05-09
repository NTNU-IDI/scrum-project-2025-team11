import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import {nextTick} from "vue";
import ResetPasswordComponent from "@/components/ResetPasswordComponent.vue";

describe("ResetPasswordComponent", () => {
    it("renders properly", async () => {
        const wrapper = mount(ResetPasswordComponent);
        await nextTick()

        // "Tilbakestille passord?" heading
        expect(wrapper.text()).toContain("Tilbakestille passord?");

        // Codeinput field
        const codeInput = wrapper.find("input[type='text']");
        expect(codeInput.exists()).toBe(true);

        //Two password input
        const passwordInputs = wrapper.findAll("input[type='password']");
        expect(passwordInputs.length).toBe(2);

        //Show password checkbox
        const cbShowPassword = wrapper.find("input[type='checkbox']")
        expect(cbShowPassword.exists()).toBe(true)
        expect((cbShowPassword.element as HTMLInputElement).checked).toBe(false)

        // "Sett nytt passord" button
        const button = wrapper.find("button");
        expect(button.exists()).toBe(true);
        expect(button.text()).toBe("Sett nytt passord");

        //Test that link shows and leads to login
        const linkToLogin = wrapper.find(".register-login-text").find(".link")
        expect(linkToLogin.exists()).toBeTruthy()
        expect(linkToLogin.attributes("to")).toBe("/login")
        expect(linkToLogin.text()).toBe("her")
    });
    it("shows password when checkbox is clicked", async () => {
        const wrapper = mount(ResetPasswordComponent);
        await nextTick()

        const cbPasswordVisibility = wrapper.find("#cbPassword")
        const iptPassword = wrapper.find("#iptPassword")
        const iptRepeatedPassword = wrapper.find("#iptRepeatedPassword")

        expect(cbPasswordVisibility.exists()).toBe(true)
        expect((cbPasswordVisibility.element as HTMLInputElement).checked).toBe(false)
        expect(iptPassword.attributes("type")).toBe("password");
        expect(iptRepeatedPassword.attributes("type")).toBe("password");

        //Got help from ChatGPT to properly test checking a box, due to DOM events not updating vmodel
        (cbPasswordVisibility.element as HTMLInputElement).checked = true;
        await cbPasswordVisibility.trigger("change")
        await nextTick()

        expect((cbPasswordVisibility.element as HTMLInputElement).checked).toBe(true)
        expect(iptPassword.attributes("type")).toBe("text")
        expect(iptRepeatedPassword.attributes("type")).toBe("text")

    });
});
