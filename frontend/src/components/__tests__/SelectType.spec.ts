import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SelectType from "@/components/map/SelectType.vue";

describe("SelectType.vue", () => {
  it('emits "close" when close icon is clicked', async () => {
    const wrapper = mount(SelectType);
    await wrapper.find(".close-icon").trigger("click");
    expect(wrapper.emitted("close")).toBeTruthy();
  });

  it('emits "add-point" when "Legg til punkt" button is clicked', async () => {
    const wrapper = mount(SelectType);
    const buttons = wrapper.findAll("button");
    await buttons[0].trigger("click");
    expect(wrapper.emitted("add-point")).toBeTruthy();
  });

  it('emits "add-event" when "Legg til hendelse" button is clicked', async () => {
    const wrapper = mount(SelectType);
    const buttons = wrapper.findAll("button");
    await buttons[1].trigger("click");
    expect(wrapper.emitted("add-event")).toBeTruthy();
  });
});
