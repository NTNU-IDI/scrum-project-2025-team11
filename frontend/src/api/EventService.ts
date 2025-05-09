import axios from 'axios';
import type { EventRequestDTO, EventResponseDTO } from '@/types/Event';

const ITEM_API_URL = "http://localhost:8080/api/events";

export class EventService {
    static async findAll(): Promise<EventResponseDTO[]> {
        const response = await axios.get<EventResponseDTO[]>(ITEM_API_URL);
        return response.data;
    }

    static async findById(id: number): Promise<EventResponseDTO> {
        const response = await axios.get<EventResponseDTO>(`${ITEM_API_URL}/${id}`);
        return response.data;
    }

    static async findActive(): Promise<EventResponseDTO[]> {
        const response = await axios.get<EventResponseDTO[]>(
        `${ITEM_API_URL}/active`
        );
        return response.data;
    }

    static async update(id: number, event: EventRequestDTO): Promise<EventResponseDTO> {
        try {
            const response = await axios.put<EventResponseDTO>(`${ITEM_API_URL}/${id}`, event);
            return response.data;
        } catch (error: any) {
            if (error.response) {
                if (error.response.status === 404) {
                    throw new Error('Fant ikke hendelse');
                } else {
                    throw new Error(`Server feil: ${error.response.status}`);
                }
            } else {
                throw new Error('Nettverksfeil');
            }
        }
    }

    static async save(event: EventRequestDTO): Promise<EventResponseDTO> {
        try {
            const response = await axios.post<EventResponseDTO>(ITEM_API_URL, event);
            return response.data;
        } catch (error: any) {
            if (error.response) {
                if (error.response.status === 400) {
                    throw new Error('Ugyldig data');
                } else {
                    throw new Error(`Server feil: ${error.response.status}`);
                }
            } else {
                throw new Error('Nettverksfeil');
            }
        }
    }

    static async delete(id: number): Promise<void> {
        await axios.delete(`${ITEM_API_URL}/${id}`, {withCredentials: true});
    }
}

