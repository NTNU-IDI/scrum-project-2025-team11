export interface EventRequestDTO {}
export interface EventResponseDTO {
    id: number;
    name: string;
    description: string;
    iconType: string;
    startDate: string;
    endDate: string;
    latitude: number;
    longitude: number;
    radius: number;
    severity: number;
}