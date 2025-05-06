import type { UserResponseDTO, UserRequestDTO} from '@/types/User';
import axios from 'axios';

const ITEM_API_URL = 'http://localhost:8080/api/users';

export class AdminService {
    static async findAll(): Promise<UserResponseDTO[]> {
        const response = await axios.get<UserResponseDTO[]>(`${ITEM_API_URL}/all`, { withCredentials: true });
        return response.data;
    }

    static async deleteUser(id: number): Promise<void> {
        await axios.delete(`${ITEM_API_URL}/${id}`);
    }

    // TODO: change to admin endpoint with role
    static async create(user: Omit<UserRequestDTO, 'id'>): Promise<void> {
        await axios.post<UserRequestDTO>(ITEM_API_URL, user);
    }
}