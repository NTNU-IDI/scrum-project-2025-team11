import type { UserResponseDTO, UserRequestDTO} from '@/types/User';
import axios from 'axios';

const USER_API_URL = 'http://localhost:8080/api/users';

export class AdminService {
    static async findAll(): Promise<UserResponseDTO[]> {
        const response = await axios.get<UserResponseDTO[]>(`${USER_API_URL}/all`, { withCredentials: true });
        return response.data;
    }

    static async deleteUser(id: number): Promise<string> {
        try {
            await axios.delete(`${USER_API_URL}/${id}` , { withCredentials: true });
            return '';
        } catch (error: any) {
            if (error.response) {
                if (error.response.status === 404) {
                    throw new Error('Bruker ble ikke funnet');
                } else {
                    throw new Error(`Server feil: ${error.response.status}`);
                }
            } else {
                throw new Error('Nettverksfeil eller serveren er nede');
            }
        }
    }

    // create admin user
    static async create(user: UserRequestDTO): Promise<string> {
        try {
            await axios.post('http://localhost:8080/auth/register-admin', user, { withCredentials: true });
            return '';
        } catch (error: any) {
            if (error.response) {
                if (error.response.status === 400) {
                    throw new Error('Ugyldig registrerings informasjon');
                } else if (error.response.status === 409) {
                    throw new Error('Enten email eller brukernavn er allerede i bruk');
                } else {
                    throw new Error(`Server feil: ${error.response.status}`);
                }
            } else {
                throw new Error('Nettverksfeil eller serveren er nede');
            }
        }
    }
}