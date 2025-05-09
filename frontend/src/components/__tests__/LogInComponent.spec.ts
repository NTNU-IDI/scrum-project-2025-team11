import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import LogInComponent from "@/components/LogInComponent.vue";

describe("LogInComponent", () => {
  it("renderer overskriften og knapp", () => {
    const wrapper = mount(LogInComponent);
    expect(wrapper.text()).toContain("Logg inn");

    const button = wrapper.find("button");
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("Logg inn");
  });

  it("viser feilmelding hvis brukernavn mangler", async () => {
    const wrapper = mount(LogInComponent);
    await wrapper.find("button").trigger("click");
    expect(wrapper.text()).toContain("Vennlgist fyll ut brukernavn.");
  });

  it("viser feilmelding hvis passord mangler", async () => {
    const wrapper = mount(LogInComponent);
    await wrapper.find("input[type='text']").setValue("testbruker");
    await wrapper.find("button").trigger("click");
    expect(wrapper.text()).toContain("Vennligst full ut passord.");
  });

  /*
  it("viser/skjuler passord basert på checkbox", async () => {
    const wrapper = mount(LogInComponent);
    const checkbox = wrapper.find("input[type='checkbox']");
    await checkbox.setValue(true);
    expect(wrapper.vm.showPassword).toBe(true);
  }); */
});
