import { describe, it, expect, beforeEach } from "vitest";
import { mount } from "@vue/test-utils";
import EditEventsComponent from "@/components/admin/manage_events/EditEventComponent.vue";
import {nextTick} from "vue";
import { createPinia, setActivePinia } from "pinia";

describe("EditEventsComponent", () => {
    beforeEach(() => {
        setActivePinia(createPinia());
        });
    it("renders edit event component properly", async () => {
        const wrapper = mount(EditEventsComponent);
        await nextTick()

        // "Rediger hendelse" heading
        expect(wrapper.text()).toContain("Rediger hendelse");

        // Five input fields
        const inputs = wrapper.findAll("input");
        expect(inputs.length).toBe(5);

        //Radius input type
        const iptRadius = wrapper.find("#radius-input")
        expect(iptRadius.attributes("type")).toBe("text")

        // "Lagre" button
        const button = wrapper.find("#save");
        expect(button.exists()).toBe(true);
        expect(button.text()).toBe("Lagre endringer");
    });

    it("Chooses severity when dropdown option is clicked", async () => {
        const wrapper = mount(EditEventsComponent);
        await nextTick()

        const severityDropdown = wrapper.find("#severity-input")
        expect(severityDropdown.exists()).toBe(true);

        await severityDropdown.setValue("3"); 

        expect((severityDropdown.element as HTMLSelectElement).value).toBe("3");
    });
});
