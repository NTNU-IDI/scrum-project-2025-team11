import type { AddressRequestDTO, AddressResponseDTO } from './Address';

export interface HouseholdUpdateDTO {
    name: string;
    memberCount: number;
}

export interface HouseholdRequestDTO {
    name: string;
    memberCount: number;
    address: AddressRequestDTO;
}

export interface HouseholdResponseDTO {
    id: number;
    name: string;
    memberCount: number;
    members: HouseholdUserDTO[];
    address: AddressResponseDTO;
}

export interface HouseholdUserDTO {
    username: string;
    firstName: string;
    lastName: string;
    email: string;
}