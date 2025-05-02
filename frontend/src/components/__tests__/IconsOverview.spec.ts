import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import IconsOverview from "@/components/map/IconsOverview.vue";

describe("IconsOverview", () => {
  it("render component", () => {
    const wrapper = mount(IconsOverview);

    // Header
    const heading = wrapper.find("h1");
    expect(heading.exists()).toBe(true);
    expect(heading.text()).toBe("Ikoner");

    // List and list items
    const ul = wrapper.find("ul");
    expect(ul.exists()).toBe(true);
    const listItems = wrapper.findAll("li");
    expect(listItems.length).toBe(6);
    expect(listItems[0].text()).toContain("Tilfluktsrom");
    expect(listItems[1].text()).toContain("Møteplass");
    expect(listItems[2].text()).toContain("Medisinks hjelp");
    expect(listItems[3].text()).toContain("Alvorlig kriseberørt område");
    expect(listItems[4].text()).toContain("Moderat kriseberørt område");
    expect(listItems[5].text()).toContain("Lett kriseberørt område");

    // Icons
    const icons = wrapper.findAll(".map-icon");
    expect(icons.length).toBe(6);
    expect(icons[0].classes()).toContain("shelter");
    expect(icons[1].classes()).toContain("assembly_point");
    expect(icons[2].classes()).toContain("medical");
    expect(icons[3].classes()).toContain("danger");
    expect(icons[3].classes()).toContain("severe");
    expect(icons[4].classes()).toContain("danger");
    expect(icons[4].classes()).toContain("moderate");
    expect(icons[5].classes()).toContain("danger");
    expect(icons[5].classes()).toContain("mild");
  });

  it("applies correct container styling", () => {
    const wrapper = mount(IconsOverview);
    const container = wrapper.find(".icons-box");
    expect(container.exists()).toBe(true);
    expect(container.classes()).toContain("icons-box");
  });
});
