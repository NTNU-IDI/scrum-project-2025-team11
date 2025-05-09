import { defineStore } from 'pinia';
import type { UserResponseDTO, UserRequestDTO } from '@/types/User';
import { AdminService } from '@/api/AdminService';

export const useAdminUserStore = defineStore('adminUsers', {
    state: () => ({
        adminUsers: [] as UserResponseDTO[],
        errorMsg: '' as string,
    }),

    actions: {
        async fetchUsers() {
            try {
                const data = await AdminService.findAll();
                this.adminUsers = data.filter(user => user.role === 'admin');
            } catch (error) {
                console.error('Kunne ikke finne adminbrukere:', error);
            }
        },
        async deleteUser(userId: number) {
           try {
                const response = await AdminService.deleteUser(userId);
                this.errorMsg = response;
                await this.fetchUsers();
            }
            catch (error) {
                this.errorMsg = error as string;
           } 
        },
        async createUser(user: UserRequestDTO) {
            try {
                const response = await AdminService.create(user);
                this.errorMsg = response;
                await this.fetchUsers(); 
            } catch (error) {
                this.errorMsg = error as string;
            }
        }
    },
    persist: true
});