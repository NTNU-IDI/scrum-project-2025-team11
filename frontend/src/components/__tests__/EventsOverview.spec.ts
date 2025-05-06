import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import EventsOverview from "@/components/map/EventsOverview.vue";

describe("EventsOverview.vue", () => {
  it("renders header with correct title", () => {
    const wrapper = mount(EventsOverview);
    expect(wrapper.find("h1").text()).toBe("Hendelser");
  });

  it("shows list when isCollapsed is false", () => {
    const wrapper = mount(EventsOverview);
    expect(wrapper.findAll("ul.map-overview-details > li")).toHaveLength(3);
  });

  it("toggles the visibility of the list when clicking on the header", async () => {
    const wrapper = mount(EventsOverview, {
      attachTo: document.body,
    });

    const list = wrapper.find("ul.map-overview-details");
    const header = wrapper.find(".map-overview-box-header");

    // Visible
    expect(list.isVisible()).toBe(true);

    // Click to collapse
    await header.trigger("click");
    await wrapper.vm.$nextTick();
    expect(list.isVisible()).toBe(false);

    // Click to be visable again
    await header.trigger("click");
    await wrapper.vm.$nextTick();
    expect(list.isVisible()).toBe(true);

    wrapper.unmount();
  });
});
