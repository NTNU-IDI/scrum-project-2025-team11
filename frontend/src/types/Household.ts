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
    address: AddressResponseDTO;
}