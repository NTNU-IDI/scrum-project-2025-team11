export interface EventRequestDTO {
    name: string;
    description: string;
    iconType: string;
    startTime: string;
    endTime?: string; 
    latitude: number;
    longitude: number;
    radius: number;
    severity: number;
}
export interface EventResponseDTO {
    id: number;
    name: string;
    description: string;
    iconType: string;
    startTime: string;
    endTime: string;
    latitude: number;
    longitude: number;
    radius: number;
    severity: number;
}