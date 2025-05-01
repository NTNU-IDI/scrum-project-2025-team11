// Regexes for validating items
const itemNameRegex = /^[a-zA-Z0-9_ ]+$/;
const itemQuantityRegex = /^[0-9]+$/;
const itemUnitRegex = /^[a-zA-Z]+$/;
const itemExpirationDateRegex = /^\d{4}-\d{2}-\d{2}$/; // YYYY-MM-DD format
const firstNameRegex = /^[a-zA-Z\s\-]+$/;
const lastNameRegex = /^[a-zA-Z\s\-]+$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const usernameRegex = /^[a-zA-Z0-9]+$/;
const householdNameRegex = /^[a-zA-Z\s\-]+$/;

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

// Function to validate first name
export function validateFirstName(name: string): boolean {
    if(!name) return false;
    return firstNameRegex.test(name);
}

// Function to validate last name
export function validateLastName(name: string): boolean {
    if(!name) return false;
    return lastNameRegex.test(name);
}

// Function to validate email
export function validateEmail(email: string): boolean {
    if(!email) return false;
    return emailRegex.test(email);
}

export function validateUsername(username: string): boolean {
    if(!username) return false
    return usernameRegex.test(username)
}

export function validateHouseholdName(householdName: string): boolean {
    if(!householdName) return false
    return householdNameRegex.test(householdName)
}

