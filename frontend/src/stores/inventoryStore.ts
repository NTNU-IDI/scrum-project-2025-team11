import { defineStore } from 'pinia';
import { InventoryService } from '@/api/InventoryService';
import type { HouseholdItemResponse, HouseholdItemRequest } from '@/types/Inventory';

export const useInventoryStore = defineStore('inventory', {
    state: () => ({
        inventory: [] as HouseholdItemResponse[],
    }),

    actions: {
        async fetchInventory(householdId: number) {
            try {
                const data = await InventoryService.list(householdId);
                this.inventory = data;
            } catch (error) {
                console.error('Error fetching inventory:', error);
            }
        },
        setItems(newItems: HouseholdItemResponse[]) {
            this.inventory = newItems;
        },
        async updateItem(householdId: number, updatedItem: HouseholdItemRequest) {
            try {
                await InventoryService.update(householdId, updatedItem.itemId, updatedItem);
            } catch (error) {
                console.error('Error updating item:', error);
            }
            await this.fetchInventory(householdId);
        },
        async upsertItem(householdId: number, newItem: HouseholdItemRequest) {
            try {
                await InventoryService.upsert(householdId, newItem);
            } catch (error) {
                console.error('Error upserting item:', error);
            }
            await this.fetchInventory(householdId);
        },
        async deleteItem(householdId: number, itemId: number, acquiredDate: string) {
            try {
                await InventoryService.remove(householdId, itemId, acquiredDate);
                this.inventory = this.inventory.filter(item => item.itemId !== itemId);
            } catch (error) {
                console.error('Failed to delete item:', error);
            }
            await this.fetchInventory(householdId);
        },
    },
    persist: true
});