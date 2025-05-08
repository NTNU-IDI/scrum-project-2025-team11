import axios from 'axios';
import type {
    HouseholdRequestDTO,
    HouseholdResponseDTO,
    HouseholdUpdateDTO,
} from '@/types/Household';

const HOUSEHOLS_API_URL = 'http://localhost:8080/api/household';

export class HouseholdService {
    static async findAll(): Promise<HouseholdResponseDTO[]> {
        const response = await axios.get<HouseholdResponseDTO[]>(`${HOUSEHOLS_API_URL}/all`);
        return response.data;
    }
    
    static async getHouseholdInformation(): Promise<HouseholdResponseDTO> {
        const response = await axios.get<HouseholdResponseDTO>(`${HOUSEHOLS_API_URL}/me`, { withCredentials: true });
        return response.data;
    }
    
    static async create(data: HouseholdRequestDTO): Promise<HouseholdResponseDTO> {
        const response = await axios.post<HouseholdResponseDTO>(HOUSEHOLS_API_URL, data);
        return response.data;
    }
    
    static async update(id: number, data: HouseholdUpdateDTO): Promise<HouseholdResponseDTO> {
        const response = await axios.put<HouseholdResponseDTO>(`${HOUSEHOLS_API_URL}/update`, data, { withCredentials: true });
        return response.data;
    }

    static async inviteToHousehold(email: string): Promise<void> {
        await axios.post(`${HOUSEHOLS_API_URL}/invite`, email, { withCredentials: true });
    }

    
}