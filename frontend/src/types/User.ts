export interface UserResponseDTO {
    id: number;
    email: string;
    username: string;
    firstName: string;
    lastName: string;
    role: string;
    householdId: number;
    householdName: string;
}
export interface UserRequestDTO {
    email: string;
    username: string;
    firstName: string;
    lastName: string;
    password: string;
    householdId: number;
}