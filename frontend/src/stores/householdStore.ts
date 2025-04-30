import {defineStore} from 'pinia'
import {HouseholdService} from '@/api/HouseholdService'

interface Household {
    id: number | null
    name: string
    memberCount: number
    addressId: string
}

export const useHouseholdStore = defineStore('household', {
    state: () => ({
        id: null as number | null,
        name: '',
        memberCount: 0,
        addressId: ''
    }),
    
    actions: {
        async setHousehold(householdId: number) {
            try {
                if (householdId === null) {
                    throw new Error('Household ID is null')
                }
                const response = await HouseholdService.findById(householdId)
                
                this.id = householdId, 
                this.name = response.name 
                this.memberCount = response.memberCount
                this.addressId = response.address.id.toString() 
                
            } catch (error) {
                console.error('Error fetching household:', error)
            }
        },
        clearHousehold() {
            this.id = null;
            this.name = '';
            this.memberCount = 0;
            this.addressId = '';
        },
        async addMember(count: number) {
            this.memberCount = count;
            try {
                if (this.id === null) {
                    throw new Error('Household ID is null')
                }
                await HouseholdService.update(this.id, {
                    ...this,
                    memberCount: count
                })
            } catch (error) {
                console.error('Error updating household member count:', error)
            }
        }
    }
})
