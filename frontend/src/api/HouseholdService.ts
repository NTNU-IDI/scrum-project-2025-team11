import axios from 'axios';
import type {
    HouseholdRequestDTO,
    HouseholdResponseDTO,
    HouseholdUpdateDTO,
} from '@/types/Household';

const HOUSEHOLS_API_URL = 'http://localhost:8080/api/household';

export class HouseholdService {
    static async findAll(): Promise<HouseholdResponseDTO[]> {
        const response = await axios.get<HouseholdResponseDTO[]>(HOUSEHOLS_API_URL);
        return response.data;
    }
    
    static async findById(id: number): Promise<HouseholdResponseDTO> {
        const response = await axios.get<HouseholdResponseDTO>(`${HOUSEHOLS_API_URL}/${id}`);
        return response.data;
    }

    static async getHouseholdInformation(): Promise<HouseholdResponseDTO> {
        const response = await axios.get<HouseholdResponseDTO>(`${HOUSEHOLS_API_URL}/me`);
        return response.data;
    }
    
    static async create(data: HouseholdRequestDTO): Promise<HouseholdResponseDTO> {
        const response = await axios.post<HouseholdResponseDTO>(HOUSEHOLS_API_URL, data);
        return response.data;
    }
    
    static async update(id: number, data: HouseholdUpdateDTO): Promise<HouseholdResponseDTO> {
        const response = await axios.put<HouseholdResponseDTO>(`${HOUSEHOLS_API_URL}/${id}`, data);
        return response.data;
    }
    
    static async delete(id: number): Promise<void> {
        await axios.delete(`${HOUSEHOLS_API_URL}/${id}`);
    }
}