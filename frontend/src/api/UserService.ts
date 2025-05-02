import type { UserResponseDTO } from '@/types/User';
import axios from 'axios';

const ITEM_API_URL = 'http://localhost:8080/api/users';

export class UserService {
    static async findAll(): Promise<UserResponseDTO[]> {
        const response = await axios.get<UserResponseDTO[]>(ITEM_API_URL);
        return response.data;
    }
}