import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import {nextTick} from "vue";
import AuthComponent from "@/components/AuthComponent.vue";

describe("AuthComponent", () => {
    it("renders properly", async () => {
        const wrapper = mount(AuthComponent);
        await nextTick()

        // "Totrinnsbekreftelse" heading
        expect(wrapper.text()).toContain("Totrinnsbekreftelse");

        // Code input
        const codeInput = wrapper.findAll("input");
        expect(codeInput.length).toBe(1);
        expect(codeInput[0].exists()).toBeTruthy()

        // "Bekreft kode" button
        const button = wrapper.find("button");
        expect(button.exists()).toBe(true);
        expect(button.text()).toBe("Bekreft kode");
    });
});
