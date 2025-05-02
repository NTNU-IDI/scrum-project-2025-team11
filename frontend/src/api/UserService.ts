import type { UserResponseDTO} from '@/types/User';
import axios from 'axios';

const ITEM_API_URL = 'http://localhost:8080/api/users';

export class UserService {
    static async findAll(): Promise<UserResponseDTO[]> {
        const response = await axios.get<UserResponseDTO[]>(ITEM_API_URL);
        return response.data;
    }

    static async deleteUser(id: number): Promise<void> {
        await axios.delete(`${ITEM_API_URL}/${id}`);
    }

    static async findById(id: number): Promise<UserResponseDTO> {
        const response = await axios.get<UserResponseDTO>(`${ITEM_API_URL}/${id}`);
        return response.data;
    }

    static async create(user: Omit<UserResponseDTO, 'id'>): Promise<UserResponseDTO> {
        const response = await axios.post<UserResponseDTO>(ITEM_API_URL, user);
        return response.data;
    }
}