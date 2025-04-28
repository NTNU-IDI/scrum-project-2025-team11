// Regexes for validating items
const itemNameRegex = /^[a-zA-Z0-9_ ]+$/;
const itemQuantityRegex = /^[0-9]+$/;
const itemUnitRegex = /^[a-zA-Z]+$/;
const itemExpirationDateRegex = /^\d{4}-\d{2}-\d{2}$/; // YYYY-MM-DD format

// Function to validate item name
export function validateItemName(name: string): boolean {
    if(!name) return false;
    return itemNameRegex.test(name);
}

// Function to validate item quantity
export function validateItemQuantity(quantity: string): boolean {
    if(!quantity) return false;
    return itemQuantityRegex.test(quantity);
}

// Function to validate item unit
export function validateItemUnit(unit: string): boolean {
    if(!unit) return false;
    return itemUnitRegex.test(unit);
}

// Function to validate item expiration date
export function validateItemExpirationDate(date: string): boolean {
    if(!date) return false;
    return itemExpirationDateRegex.test(date);
}
