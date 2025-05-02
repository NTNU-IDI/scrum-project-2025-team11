import { defineStore } from 'pinia';
import { InventoryService } from '@/api/InventoryService';
import type { HouseholdItemResponse, HouseholdItemRequest } from '@/types/Inventory';
import type { UserResponseDTO } from '@/types/User';
import { UserService } from '@/api/UserService';

export const useAdminUserStore = defineStore('adminUsers', {
    state: () => ({
        adminUsers: [] as UserResponseDTO[],
    }),

    actions: {
        async fetchUsers() {
            try {
                const data = await UserService.findAll();
                this.adminUsers = data.filter(user => user.role === 'admin');
            } catch (error) {
                console.error('Error fetching admin users:', error);
            }
        },
        async deleteUser(userId: number) {
           try {
                await UserService.deleteUser(userId);
                this.adminUsers = this.adminUsers.filter(user => user.id !== userId);
            }
            catch (error) {
           } 
        },
    },
    persist: true
});