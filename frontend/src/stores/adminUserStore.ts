import { defineStore } from 'pinia';
import type { UserResponseDTO, UserRequestDTO } from '@/types/User';
import { AdminService } from '@/api/AdminService';

export const useAdminUserStore = defineStore('adminUsers', {
    state: () => ({
        adminUsers: [] as UserResponseDTO[],
    }),

    actions: {
        async fetchUsers() {
            try {
                const data = await AdminService.findAll();
                this.adminUsers = data.filter(user => user.role === 'admin');
            } catch (error) {
                console.error('Error fetching admin users:', error);
            }
        },
        async deleteUser(userId: number) {
           try {
                await AdminService.deleteUser(userId);
                await this.fetchUsers();
            }
            catch (error) {
           } 
        },

        async createUser(user: Omit<UserRequestDTO, 'id'>) {
            try {
                await AdminService.create(user);
                await this.fetchUsers(); 
            } catch (error) {
                console.error('Error adding admin user:', error);
            }
        }
    },
    persist: true
});