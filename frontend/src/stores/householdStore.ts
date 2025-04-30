import {defineStore} from 'pinia'

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
        addressId: '',
    }),
    
    actions: {
        setHousehold(household: Household) {
            this.id = household.id;
            this.name = household.name;
            this.memberCount = household.memberCount;
            this.addressId = household.addressId;
        },
        clearHousehold() {
            this.id = null;
            this.name = '';
            this.memberCount = 0;
            this.addressId = '';
        },
        addMember() {
            this.memberCount++;
        }
    }
})
