import axios from 'axios';

import type { Item } from '@/types/Item';

const ITEM_API_URL = 'http://localhost:8080/api/items';

export class ItemService {
    static async findAll(): Promise<Item[]> {
        const response = await axios.get<Item[]>(ITEM_API_URL);
        return response.data;
      }
    
      static async findById(id: number): Promise<Item> {
        const response = await axios.get<Item>(`${ITEM_API_URL}/${id}`);
        return response.data;
      }
    
      static async create(item: Omit<Item, 'id'>): Promise<Item> {
        const response = await axios.post<Item>(ITEM_API_URL, item);
        return response.data;
      }
    
      static async update(id: number, item: Omit<Item, 'id'>): Promise<Item> {
        const response = await axios.put<Item>(`${ITEM_API_URL}/${id}`, item);
        return response.data;
      }
    
      static async delete(id: number): Promise<void> {
        await axios.delete(`${ITEM_API_URL}/${id}`);
      }
}