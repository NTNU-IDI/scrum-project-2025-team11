import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import LogInView from "@/components/LogInComponent.vue";
import {nextTick} from "vue";

describe("LogInComponent", () => {
  it("renders login component properly", async () => {
    const wrapper = mount(LogInView);
    await nextTick()

    // "Logg inn" heading
    expect(wrapper.text()).toContain("Logg inn");

    // Two input fields, one checkbox
    const inputs = wrapper.findAll("input");
    expect(inputs.length).toBe(3);

    // Password visibility checkbox
    const cbPasswordVisibility = wrapper.find("#cbPassword")
    expect(cbPasswordVisibility.exists()).toBe(true)
    expect((cbPasswordVisibility.element as HTMLInputElement).checked).toBe(false)

    //Password input type
    const iptPassword = wrapper.find("#iptPassword")
    expect(iptPassword.attributes("type")).toBe("password")

    // "Logg inn" button
    const button = wrapper.find("button");
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("Logg inn");

    // Router-link to register
    const routerLinkToRegister = wrapper.find("#linkToRegister");
    expect(routerLinkToRegister.exists()).toBe(true);
    expect(routerLinkToRegister.attributes("to")).toBe("/register");

    //Router-link to forgotten password
    const routerLinkToForgot = wrapper.find("#linkToForgot");
    expect(routerLinkToForgot.exists()).toBe(true);
    expect(routerLinkToForgot.attributes("to")).toBe("/forgot");
  });
  it("shows password when checkbox is clicked", async () => {
    const wrapper = mount(LogInView);
    await nextTick()

    const cbPasswordVisibility = wrapper.find("#cbPassword")
    const iptPassword = wrapper.find("#iptPassword")

    expect(cbPasswordVisibility.exists()).toBe(true)
    expect((cbPasswordVisibility.element as HTMLInputElement).checked).toBe(false)
    expect(iptPassword.attributes("type")).toBe("password");

    //Got help from ChatGPT to properly test checking a box, due to DOM events not updating vmodel
    (cbPasswordVisibility.element as HTMLInputElement).checked = true;
    await cbPasswordVisibility.trigger("change")
    await nextTick()

    expect((cbPasswordVisibility.element as HTMLInputElement).checked).toBe(true)
    expect(iptPassword.attributes("type")).toBe("text")
  });
});
