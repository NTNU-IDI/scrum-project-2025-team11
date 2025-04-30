export interface HouseholdItemRequest {
    itemId: number;
    quantity: number;
    unit: string;
    acquiredDate: string; 
    expirationDate?: string;
}
  
export interface HouseholdItemResponse {
    householdId: number;
    itemId: number;
    quantity: number;
    unit: string;
    acquiredDate: string;
    expirationDate?: string;
    itemName: string;
    itemDescription?: string;
}
  
export interface UpsertInventoryRequest {
    itemId?: number;
    name?: string;
    description?: string;
    quantity: number;
    unit: string;
    acquiredDate: string;
    expirationDate?: string;
}