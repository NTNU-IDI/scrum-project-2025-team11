import ViewSingleArticleComponent from "@/components/household/ViewSingleItemComponent.vue";
import { useItemTypeStore } from "@/stores/itemStore";
import * as formatDateUtils from "@/utils/formatDate";
import { mount } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, it, vi } from "vitest";

interface Item {
  quantity: number;
  unit: string;
  expirationDate: Date;
}

vi.mock("@/utils/formatDate", () => ({
  formatDate: vi.fn().mockReturnValue("2026-10-01"),
  formatDateToList: vi.fn().mockReturnValue(["2026", "10", "01"]),
}));

describe("ViewSingleArticleComponent", () => {
  let store: ReturnType<typeof useItemTypeStore>;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useItemTypeStore();

    // Set mock store
    vi.spyOn(store, "id", "get").mockReturnValue(123);
    vi.spyOn(store, "name", "get").mockReturnValue("Hermetiske tomater");
    vi.spyOn(store, "items", "get").mockReturnValue([
      { quantity: 6, unit: "kg", expirationDate: new Date("2026-10-01") },
      { quantity: 1, unit: "kg", expirationDate: new Date("2026-05-01") },
    ] as Item[]);
  });

  it("renders correctly in view mode", () => {
    vi.spyOn(store, "isEditMode", "get").mockReturnValue(false);
    const wrapper = mount(ViewSingleArticleComponent);

    // Component
    expect(wrapper.find("h1").text()).toBe("Informasjon om artikkel");
    expect(wrapper.find("h2").text()).toBe("Hermetiske tomater");

    // Article cards
    const articleCards = wrapper.findAll(".article-card");
    expect(articleCards.length).toBe(2);
    expect(articleCards[0].find("p").text()).toBe("6 kg");
    expect(articleCards[0].find(".exp-date").text().includes("Utløper:")).toBe(
      true
    );
    expect(
      articleCards[0].find(".exp-date").text().includes("2026-10-01")
    ).toBe(true);

    // Edit mode elements are not present
    expect(wrapper.find("#quantity-input").exists()).toBe(false);
    expect(wrapper.find(".delete-button").exists()).toBe(false);
  });

  it("renders correctly in edit mode", () => {
    vi.spyOn(store, "isEditMode", "get").mockReturnValue(true);
    const wrapper = mount(ViewSingleArticleComponent);

    // Component
    expect(wrapper.find("h1").text()).toBe("Informasjon om artikkel");
    expect(wrapper.find("h2").text()).toBe("Hermetiske tomater");

    // Article cards
    const itemCards = wrapper.findAll(".item-card");
    expect(itemCards.length).toBe(2);
    const firstItemCard = itemCards[0];
    const quantityInput = firstItemCard.find("#quantity-input");
    expect(quantityInput.exists()).toBe(true);
    expect((quantityInput.element as HTMLInputElement).value).toBe("6");

    // Delete buttons
    const deleteButtons = wrapper.findAll(".delete-button");
    expect(deleteButtons.length).toBe(2);
  });

  it("handles delete item functionality", async () => {
    vi.spyOn(store, "isEditMode", "get").mockReturnValue(true);
    const deleteItemSpy = vi.fn();
    const wrapper = mount(ViewSingleArticleComponent);
    wrapper.vm.deleteItem = deleteItemSpy;

    // Click delete button and check if method was called
    await wrapper.find(".delete-button").trigger("click");
    expect(deleteItemSpy).toHaveBeenCalled();
  });

  it("displays formatted dates correctly", () => {
    vi.spyOn(store, "isEditMode", "get").mockReturnValue(false);
    mount(ViewSingleArticleComponent);
    expect(formatDateUtils.formatDate).toHaveBeenCalledWith(expect.any(Date));
  });
});
