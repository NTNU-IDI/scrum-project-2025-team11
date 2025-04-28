import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import LogInView from "@/components/LogInComponent.vue";

describe("LogInComponent", () => {
  it("renders login component properly", () => {
    const wrapper = mount(LogInView);

    // "Logg inn" heading
    expect(wrapper.text()).toContain("Logg inn");

    // Two input fields
    const inputs = wrapper.findAll("input");
    expect(inputs.length).toBe(2);

    // "Logg inn" button
    const button = wrapper.find("button");
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("Logg inn");

    // Router-link
    const routerLink = wrapper.find("router-link");
    expect(routerLink.exists()).toBe(true);
    expect(routerLink.attributes("to")).toBe("/register");
  });
});
