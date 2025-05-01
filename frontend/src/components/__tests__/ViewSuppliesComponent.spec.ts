import { mount, flushPromises } from "@vue/test-utils";
import { createPinia, setActivePinia } from "pinia";
import { describe, expect, it, vi, beforeEach } from "vitest";
import ViewSuppliesComponent from "@/components/household/ViewSuppliesComponent.vue";
import { useItemTypeStore } from "@/stores/itemStore";
import { useHouseholdStore } from "@/stores/householdStore";
import { useInventoryStore } from "@/stores/inventoryStore";
import { ItemService } from "@/api/ItemService";

vi.mock("@/api/ItemService", () => ({
    ItemService: {
      findById: vi.fn((id: number) => Promise.resolve({ name: `Item ${id}` })),
    },
}));

describe("ViewSuppliesComponent", () => {
	let itemTypeStore: ReturnType<typeof useItemTypeStore>;
	let householdStore: ReturnType<typeof useHouseholdStore>;
	let inventoryStore: ReturnType<typeof useInventoryStore>;

	beforeEach(() => {
		setActivePinia(createPinia());
		itemTypeStore = useItemTypeStore();
		householdStore = useHouseholdStore();
		inventoryStore = useInventoryStore();

		householdStore.setHousehold(1);

		inventoryStore.inventory = [
		{ householdId: 1, itemName: "Vann", itemId: 1, quantity: 10, unit: "liter", acquiredDate: "2024-01-01" },
		{ householdId: 1, itemName: "Bønner", itemId: 2, quantity: 5, unit: "kg", acquiredDate: "2024-02-01" },
		];
	});

	it("renders header correctly", async () => {
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();
		expect(wrapper.find("h1").text()).toBe("Beredskapslager");
	});

	it("displays correct number of items", async () => {
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();
		const items = wrapper.findAll(".item-card");
		expect(items.length).toBe(2);
	});

	it("shows quantity and unit correctly", async () => {
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();
		expect(wrapper.find(".quantity").text()).toBe("10 liter");
	});

	it("updates selected item when clicked", async () => {
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();
		const card = wrapper.findAll(".article-card")[1];
		await card.trigger("click");

		expect(itemTypeStore.id).toBe(2);
		expect(itemTypeStore.name).toBe("Item 2");

		const activeCards = wrapper.findAll(".article-card.active");
		expect(activeCards.length).toBe(1);
		expect(activeCards[0].text()).toContain("Item 2");
	});

	it("toggles edit mode and button text", async () => {
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();

		const button = wrapper.find(".dark-button");
		expect(button.text()).toBe("Endre lager");

		await button.trigger("click");
		expect(wrapper.vm.isEditMode).toBe(true);
		expect(button.text()).toBe("Large");
		expect(button.classes()).toContain("active");

		await button.trigger("click");
		expect(wrapper.vm.isEditMode).toBe(false);
		expect(button.text()).toBe("Endre lager");
	});

	it("shows delete buttons in edit mode", async () => {
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();

		expect(wrapper.findAll(".delete-button").length).toBe(0);

		await wrapper.find(".dark-button").trigger("click");
		await flushPromises();

		const deleteButtons = wrapper.findAll(".delete-button");
		expect(deleteButtons.length).toBe(2);
		expect(deleteButtons[0].text()).toBe("X");
	});

	it("handles empty inventory gracefully", async () => {
		inventoryStore.inventory = [];
		const wrapper = mount(ViewSuppliesComponent);
		await flushPromises();
		expect(wrapper.findAll(".item-card").length).toBe(0);
	});
});
