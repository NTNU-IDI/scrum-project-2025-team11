import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import {nextTick} from "vue";
import ForgotPasswordComponent from "@/components/ForgotPasswordComponent.vue";

describe("ForgotPasswordComponent", () => {
    it("renders properly", async () => {
        const wrapper = mount(ForgotPasswordComponent);
        await nextTick()

        // "Tilbakestille passord?" heading
        expect(wrapper.text()).toContain("Tilbakestille passord?");

        // Email input
        const codeInput = wrapper.findAll("input");
        expect(codeInput.length).toBe(1);
        expect(codeInput[0].exists()).toBeTruthy()

        // "Send kode" button
        const button = wrapper.find("button");
        expect(button.exists()).toBe(true);
        expect(button.text()).toBe("Send kode");

        //Test that link shows and leads to login
        const linkToLogin = wrapper.find(".register-login-text").find(".link")
        expect(linkToLogin.exists()).toBeTruthy()
        expect(linkToLogin.attributes("to")).toBe("/login")
        expect(linkToLogin.text()).toBe("her")
    });
});
