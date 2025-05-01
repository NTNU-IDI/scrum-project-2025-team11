export interface AddressRequestDTO {
    street: string;
    postalCode: string;
    city: string;
    latitude: number;
    longitude: number;
}

export interface AddressResponseDTO {
    id: number;
    street: string;
    postalCode: string;
    city: string;
    latitude: number;
    longitude: number;
}