import {defineStore} from 'pinia'
import {HouseholdService} from '@/api/HouseholdService'
import type {HouseholdUserDTO} from '@/types/Household'

export const useHouseholdStore = defineStore('household', {
    state: () => ({
        id: null as number | null,
        name: '',
        memberCount: 0,
        members: [] as Array<HouseholdUserDTO>,
        address: '',
        errrMsg: '' 
    }),
    
    actions: {
        async fetchHousehold() {
            try {
                await HouseholdService.getHouseholdInformation().then((response) => {
                    this.id = response.id, 
                    this.name = response.name 
                    this.memberCount = response.memberCount
                    this.members = response.members
                    this.address = response.address.street
                })
                .catch((error) => {
                    if(error.response.status === 404) {
                        this.errrMsg = 'Household not found'
                    }
                });
                
                
                
                
            } catch (error) {
                console.error('Error fetching household:', error)
            }
        },
        async inviteMember(email: string) {
            try {
                await HouseholdService.inviteToHousehold(email);
                this.errrMsg = '';  
            } catch (error: any) {
                if(error.response && error.response.data && error.response.data.error) {
                    this.errrMsg = error.response.data.error;
                } else {
                    this.errrMsg = 'Error inviting member'
                }
            }
        },

        clearHousehold() {
            this.id = null;
            this.name = '';
            this.memberCount = 0;
            this.members = [];
            this.address = '';
            this.errrMsg = '';
        },
    }
})
