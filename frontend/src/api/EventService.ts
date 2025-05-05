import axios from 'axios';
import type { EventResponseDTO } from '@/types/Event';

const ITEM_API_URL = 'http://localhost:8080/api/events';

export class EventService {
    static async findAll(): Promise<EventResponseDTO[]> {
        const response = await axios.get<EventResponseDTO[]>(ITEM_API_URL);
        return response.data;
    }

    static async findById(id: number): Promise<EventResponseDTO> {
        const response = await axios.get<EventResponseDTO>(`${ITEM_API_URL}/${id}`);
        return response.data;
    }
}