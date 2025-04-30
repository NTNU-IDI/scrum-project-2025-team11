import axios from 'axios';

import type {
    HouseholdItemRequest,
    HouseholdItemResponse,
    UpsertInventoryRequest,
} from '@/types/Inventory';

const BASE_API_URL = 'http://localhost:8080/api';

export class InventoryService {
    static async list(householdId: number): Promise<HouseholdItemResponse[]> {
        const res = await axios.get(`${BASE_API_URL}/households/${householdId}/items`);
        return res.data;
    }

    static async add(householdId: number, item: HouseholdItemRequest): Promise<HouseholdItemResponse> {
        const res = await axios.post(`${BASE_API_URL}/households/${householdId}/items`, item);
        return res.data;
    }

    static async update(householdId: number, itemId: number, item: HouseholdItemRequest): Promise<HouseholdItemResponse> {
        const res = await axios.put(`${BASE_API_URL}/households/${householdId}/items/${itemId}`, item);
        return res.data;
    }
    
    static async upsert(householdId: number, item: UpsertInventoryRequest): Promise<HouseholdItemResponse> {
        const res = await axios.post(`${BASE_API_URL}/households/${householdId}/items/upsert`, item);
        return res.data;
    }
    
    static async remove(householdId: number, itemId: number, acquiredDate: string): Promise<void> {
        await axios.delete(`${BASE_API_URL}/households/${householdId}/items/${itemId}?acquiredDate=${acquiredDate}`);
    }
}