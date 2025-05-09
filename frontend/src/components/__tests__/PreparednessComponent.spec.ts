import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import PreparednessComponent from "@/components/PreparednessComponent.vue";
describe("PreparednessComponent", () => {
  it("viser overskriftstekst", () => {
    const wrapper = mount(PreparednessComponent);
    expect(wrapper.text()).toContain(
      "Vet du hva du bør gjøre FØR, UNDER og ETTER en krise?"
    );
  });
  it("whether all the three crisis phases are showing", () => {
    const wrapper = mount(PreparednessComponent);
    const titles = wrapper.findAll("h3").map(el => el.text().trim());
    expect(titles).toContain("FØR");
    expect(titles).toContain("UNDER");
    expect(titles).toContain("ETTER");
  });
  it(" whether all the icons for crises are showing ", () => {
    const wrapper = mount(PreparednessComponent);
    const iconClasses = wrapper.findAll("i").map(i => i.classes().join(" "));
    const containsArrowLeft = iconClasses.some(cls => cls.includes("fa-arrow-left"));
    const containsWarning = iconClasses.some(cls => cls.includes("fa-warning"));
    const containsArrowRight = iconClasses.some(cls => cls.includes("fa-arrow-right"));
    expect(containsArrowLeft).toBe(true);
    expect(containsWarning).toBe(true);
    expect(containsArrowRight).toBe(true);
  });
  it("whether the text 'ETTER' is correct", () => {
    const wrapper = mount(PreparednessComponent);
    const etterBox = wrapper.findAll(".box").at(2);
    expect(etterBox?.text()).toContain("Etter en krise bør du evaluere og lære");
  });
});