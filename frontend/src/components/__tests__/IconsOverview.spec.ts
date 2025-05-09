import { describe, it, expect, beforeEach } from "vitest";
import { mount } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import IconsOverview from "@/components/map/IconsOverview.vue";

describe("IconsOverview", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  it("render component", () => {
    const wrapper = mount(IconsOverview, {
      attachTo: document.body,
    });

    // Header
    const heading = wrapper.find("h1.title-map");
    expect(heading.exists()).toBe(true);
    expect(heading.text()).toBe("Ikoner");

    // List and list items
    const ul = wrapper.find("ul.map-overview-details");
    expect(ul.exists()).toBe(true);

    const listItems = wrapper.findAll("li.map-li");
    expect(listItems.length).toBe(3);
    expect(listItems[0].text()).toContain("Tilfluktsrom");
    expect(listItems[1].text()).toContain("Møteplass");
    expect(listItems[2].text()).toContain("Medisinsk hjelp");

    // Icons
    const icons = wrapper.findAll(".map-icon");
    expect(icons.length).toBe(3);
    expect(icons[0].classes()).toContain("shelter");
    expect(icons[1].classes()).toContain("assembly_point");
    expect(icons[2].classes()).toContain("medical");

    wrapper.unmount();
  });

  /*
  it("toggles visibility of the list on header click", async () => {
    const wrapper = mount(IconsOverview, {
      attachTo: document.body,
    });

    const ul = wrapper.find("ul.map-overview-details");
    const header = wrapper.find(".map-overview-box-header");

    expect(ul.isVisible()).toBe(true);

    await header.trigger("click");
    await wrapper.vm.$nextTick();
    expect(ul.isVisible()).toBe(false);

    await header.trigger("click");
    await wrapper.vm.$nextTick();
    expect(ul.isVisible()).toBe(true);

    wrapper.unmount();
  });
  */
});
