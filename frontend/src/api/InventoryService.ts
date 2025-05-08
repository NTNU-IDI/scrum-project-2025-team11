import axios from 'axios';

import type {
    HouseholdItemRequest,
    HouseholdItemResponse,
    UpsertInventoryRequest,
} from '@/types/Inventory';

const HOUSEHOLD_API_URL = 'http://localhost:8080/api/households/items';

export class InventoryService {
    static async list(): Promise<HouseholdItemResponse[]> {
        const res = await axios.get(`${HOUSEHOLD_API_URL}`, { withCredentials: true });
        return res.data;
    }

    static async add(item: HouseholdItemRequest): Promise<HouseholdItemResponse> {
        const res = await axios.post(`${HOUSEHOLD_API_URL}`, item, { withCredentials: true });
        return res.data;
    }

    static async update(itemId: number, item: HouseholdItemRequest): Promise<HouseholdItemResponse> {
        const res = await axios.put(`${HOUSEHOLD_API_URL}/${itemId}/${item.acquiredDate}`, item, { withCredentials: true });
        return res.data;
    }
    
    static async upsert(item: UpsertInventoryRequest): Promise<HouseholdItemResponse> {
        const res = await axios.post(`${HOUSEHOLD_API_URL}/upsert`, item, { withCredentials: true });
        return res.data;
    }
    
    static async remove(itemId: number, acquiredDate: string): Promise<void> {
        await axios.delete(`${HOUSEHOLD_API_URL}/${itemId}/${acquiredDate}`, { withCredentials: true });
    }
}