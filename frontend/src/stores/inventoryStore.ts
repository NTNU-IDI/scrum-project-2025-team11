import { defineStore } from 'pinia';
import { InventoryService } from '@/api/InventoryService';
import type { HouseholdItemResponse, HouseholdItemRequest } from '@/types/Inventory';

export const useInventoryStore = defineStore('inventory', {
    state: () => ({
        inventory: [] as HouseholdItemResponse[],
    }),

    actions: {
        async fetchInventory() {
            try {
                const data = await InventoryService.list();
                this.inventory = data;
            } catch (error) {
                console.error('Error fetching inventory:', error);
            }
        },
        setItems(newItems: HouseholdItemResponse[]) {
            this.inventory = newItems;
        },
        async updateItem(updatedItem: HouseholdItemRequest) {
            try {
                await InventoryService.update(updatedItem.itemId, updatedItem);
            } catch (error) {
                console.error('Error updating item:', error);
            }
            await this.fetchInventory();
        },
        async upsertItem(newItem: HouseholdItemRequest) {
            try {
                await InventoryService.upsert(newItem);
            } catch (error) {
                console.error('Error upserting item:', error);
            }
            await this.fetchInventory();
        },
        async deleteItem(itemId: number, acquiredDate: string) {
            try {
                await InventoryService.remove(itemId, acquiredDate);
                this.inventory = this.inventory.filter(item => item.itemId !== itemId);
            } catch (error) {
                console.error('Failed to delete item:', error);
            }
            await this.fetchInventory();
        },
    },
    persist: true,
});