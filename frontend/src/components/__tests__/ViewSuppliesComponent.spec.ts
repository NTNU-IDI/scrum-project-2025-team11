import { mount } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import { describe, expect, it, vi, beforeEach } from "vitest";
import ViewSuppliesComponent from "@/components/household/ViewSuppliesComponent.vue";
import { useItemTypeStore } from "@/stores/itemStore";

describe("ViewSuppliesComponent", () => {
  let store: ReturnType<typeof useItemTypeStore>;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useItemTypeStore();
  });

  it("renders component with correct header", () => {
    const wrapper = mount(ViewSuppliesComponent);
    expect(wrapper.find("h1").text()).toBe("Beredskapslager");
  });

  it("displays all supply items correctly", () => {
    const wrapper = mount(ViewSuppliesComponent);
    const itemCards = wrapper.findAll(".item-card");
    expect(itemCards.length).toBe(7);
  });

  it("show correct quantity and unit for item", () => {
    const wrapper = mount(ViewSuppliesComponent);
    const firstItem = wrapper.find(".quantity");
    expect(firstItem.text()).toContain("10 liter");
  });

  it("calls chooseItemType when an item is clicked", async () => {
    const wrapper = mount(ViewSuppliesComponent);
    const chooseItemTypeSpy = vi.spyOn(wrapper.vm, "chooseItemType");
    await wrapper.findAll(".article-card")[0].trigger("click");
    expect(chooseItemTypeSpy).toHaveBeenCalled();
    expect(chooseItemTypeSpy).toHaveBeenCalledWith(
      1,
      "Vann",
      expect.arrayContaining([
        expect.objectContaining({ quantity: 6, unit: "kg" }),
        expect.objectContaining({ quantity: 1, unit: "kg" }),
      ])
    );
  });

  it("updates store when chooseItemType is called", async () => {
    const wrapper = mount(ViewSuppliesComponent);
    const setItemTypeSpy = vi.spyOn(store, "setItemType");
    await wrapper.vm.chooseItemType(2, "Hermetiske tomater", [
      { quantity: 6, unit: "kg", expirationDate: new Date("2026-10-01") },
    ]);
    expect(setItemTypeSpy).toHaveBeenCalledWith(
      2,
      "Hermetiske tomater",
      expect.arrayContaining([
        expect.objectContaining({ quantity: 6, unit: "kg" }),
      ])
    );
    expect(wrapper.vm.selectedTypeId).toBe(2);
  });

  it("toggles edit mode when button is clicked", async () => {
    const wrapper = mount(ViewSuppliesComponent);
    const toggleEditModeSpy = vi.spyOn(store, "toggleEditMode");
    expect(wrapper.vm.isEditMode).toBe(false);
    expect(wrapper.find(".dark-button").text()).toBe("Endre lager");
    await wrapper.find(".dark-button").trigger("click");
    expect(toggleEditModeSpy).toHaveBeenCalled();
    expect(wrapper.vm.isEditMode).toBe(true);
    expect(wrapper.find(".dark-button").text()).toBe("Large");
    expect(wrapper.find(".dark-button").classes()).toContain("active");
  });

  it("shows delete buttons in edit mode", async () => {
    const wrapper = mount(ViewSuppliesComponent);
    expect(wrapper.findAll(".delete-button").length).toBe(0);
    await wrapper.find(".dark-button").trigger("click");
    const deleteButtons = wrapper.findAll(".delete-button");
    expect(deleteButtons.length).toBe(7);
    expect(deleteButtons[0].text()).toBe("X");
  });

  it("apply class to selected item", async () => {
    const wrapper = mount(ViewSuppliesComponent);
    await wrapper.findAll(".article-card")[1].trigger("click");
    const activeCards = wrapper.findAll(".article-card.active");
    expect(activeCards.length).toBe(1);
    expect(activeCards[0].text()).toContain("Hermetiske tomater");
  });

  it("handle empty items array", () => {
    const wrapper = mount(ViewSuppliesComponent, {
      global: {
        mocks: {
          items: [],
        },
      },
    });
    expect(wrapper.findAll(".item-card").length).toBe(0);
  });

  it("update text on button when mode changes", async () => {
    const wrapper = mount(ViewSuppliesComponent);
    const button = wrapper.find(".dark-button");
    expect(button.text()).toBe("Endre lager");
    await button.trigger("click");
    expect(button.text()).toBe("Large");
    await button.trigger("click");
    expect(button.text()).toBe("Endre lager");
  });
});
