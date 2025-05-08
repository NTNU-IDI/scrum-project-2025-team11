import ViewSingleItemComponent from "@/components/household/ViewSingleItemComponent.vue";
import { useInventoryStore } from "@/stores/inventoryStore";
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

describe("ViewSingleItemComponent", () => {
  	let store: ReturnType<typeof useItemTypeStore>;

	const mockInventory = [
		{
		  	itemId: 123,
		  	householdId: 1,
		  	itemName: "Hermetiske tomater",
		  	quantity: 6,
		  	unit: "kg",
		  	expirationDate: "2026-10-01",
		  	acquiredDate: "2025-05-01",
		},
		{
			itemId: 123,
			householdId: 1,
			itemName: "Hermetiske tomater",
			quantity: 4,
			unit: "kg",
			expirationDate: "2026-09-01",
			acquiredDate: "2025-04-01",
		},
	];
  	beforeEach(() => {
    	setActivePinia(createPinia());

		store = useItemTypeStore();
		const inventoryStore = useInventoryStore();

		vi.spyOn(store, "id", "get").mockReturnValue(123);
		vi.spyOn(store, "name", "get").mockReturnValue("Hermetiske tomater");

		inventoryStore.inventory = mockInventory;
  	});

  	// Check if the component renders correctly in view mode
  	it("renders correctly in view mode", () => {
    	vi.spyOn(store, "isEditMode", "get").mockReturnValue(false);
		const wrapper = mount(ViewSingleItemComponent);

		expect(wrapper.find("h1").text()).toBe("Informasjon om artikkel");
		expect(wrapper.find("h2").text()).toBe("Hermetiske tomater");

		const articleCards = wrapper.findAll(".article-card");
		expect(articleCards.length).toBe(2);
		expect(articleCards[0].find("p").text()).toBe("6 kg");

		const expDateText = articleCards[0].find(".exp-date").text();
		expect(expDateText).toContain("Utløper:");
		expect(expDateText).toContain("2026-10-01");

		expect(wrapper.find("#quantity-input").exists()).toBe(false);
		expect(wrapper.find(".delete-button").exists()).toBe(false);
  	});

	// Check if the component renders correctly in edit mode
	it("renders correctly in edit mode", () => {
		vi.spyOn(store, "isEditMode", "get").mockReturnValue(true);
		const wrapper = mount(ViewSingleItemComponent);

		expect(wrapper.find("h1").text()).toBe("Informasjon om artikkel");
		expect(wrapper.find("h2").text()).toBe("Hermetiske tomater");

		const itemCards = wrapper.findAll(".item-card");
		expect(itemCards.length).toBe(2);

		const quantityInput = itemCards[0].find("#quantity-input");
		expect(quantityInput.exists()).toBe(true);
		expect((quantityInput.element as HTMLInputElement).value).toBe("6");

		const deleteButtons = wrapper.findAll(".delete-button");
		expect(deleteButtons.length).toBe(2);
	});

	// Check if the component handles delete item functionality
	it("handles delete item functionality", async () => {
		vi.spyOn(store, "isEditMode", "get").mockReturnValue(true);
		const wrapper = mount(ViewSingleItemComponent);
		const deleteSpy = vi.spyOn(wrapper.vm as any, "deleteItem");

		await wrapper.find(".delete-button").trigger("click");

		expect(deleteSpy).toHaveBeenCalled();
	});
});