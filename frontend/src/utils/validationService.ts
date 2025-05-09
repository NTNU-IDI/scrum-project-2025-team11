// Regexes for validating items
const itemNameRegex = /^[æøåÆØÅa-zA-Z0-9_ ]+$/;
const itemQuantityRegex = /^[0-9]+$/;
const itemUnitRegex = /^[æøåÆØÅa-zA-Z]+$/;
const itemExpirationDateRegex = /^\d{4}-\d{2}-\d{2}$/; // YYYY-MM-DD format
const firstNameRegex = /^[æøåÆØÅa-zA-Z\s\-]+$/;
const lastNameRegex = /^[æøåÆØÅa-zA-Z\s\-]+$/;
const passwordRegex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
//Passwordregex ensures at least 8 characters, one uppercase letter, one lowercase letter, one number and one
//special character. Gotten from: https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a

// Regexes for user
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const usernameRegex = /^[a-zA-Z0-9]+$/;
const householdNameRegex = /^[æøåÆØÅa-zA-Z\s\-]+$/;

// Regexes for validating icons (interest points)
const pointNameRegex = /^[æøåÆØÅa-zA-Z0-9_ ]+$/;
const pointDescriptionRegex = /^.{5,100}$/;
const iconTypeRegex =
  /^(shelter|assembly_point|medical|normal|none|point|danger)$/; // One of these
const coordinateRegex = /^-?\d+(\.\d+)?$/; // Integer or decimal

// Function to validate item name
export function validateItemName(name: string): boolean {
  if (!name) return false;
  return itemNameRegex.test(name);
}

// Function to validate item quantity
export function validateItemQuantity(quantity: string): boolean {
  if (!quantity) return false;
  return itemQuantityRegex.test(quantity);
}

// Function to validate item unit
export function validateItemUnit(unit: string): boolean {
  if (!unit) return false;
  return itemUnitRegex.test(unit);
}

// Function to validate item expiration date
export function validateItemExpirationDate(date: string): boolean {
  if (!date) return false;
  return itemExpirationDateRegex.test(date);
}

// Function to validate first name
export function validateFirstName(name: string): boolean {
  if (!name) return false;
  return firstNameRegex.test(name);
}

// Function to validate last name
export function validateLastName(name: string): boolean {
  if (!name) return false;
  return lastNameRegex.test(name);
}

// Function to validate email
export function validateEmail(email: string): boolean {
  if (!email) return false;
  return emailRegex.test(email);
}

export function validateUsername(username: string): boolean {
  if (!username) return false;
  return usernameRegex.test(username);
}

export function validateHouseholdName(householdName: string): boolean {
  if (!householdName) return false;
  return householdNameRegex.test(householdName);
}

// Function to validate point name
export function validatePointName(name: string): boolean {
  if (!name) return false;
  return pointNameRegex.test(name.trim());
}

// Function to validate point description
export function validatePointDescription(desc: string): boolean {
  if (!desc) return false;
  return pointDescriptionRegex.test(desc.trim());
}

// Function to validate icon type
export function validateIconType(type: string): boolean {
  return !!type && iconTypeRegex.test(type);
}

// Function to validate latitude
export function validateLatitude(lat: number): boolean {
  if (!lat) return false;

  const decimalPlacesRegex = /^-?\d+(\.\d{1,7})?$/;
  if (!decimalPlacesRegex.test(lat.toString())) return false;

  return coordinateRegex.test(lat.toString()) && lat >= -90 && lat <= 90;
}

// Function to validate longitude
export function validateLongitude(lon: number): boolean {
  if (!lon) return false;

  const decimalPlacesRegex = /^-?\d+(\.\d{1,7})?$/;
  if (!decimalPlacesRegex.test(lon.toString())) return false;

  return coordinateRegex.test(lon.toString()) && lon >= -180 && lon <= 180;
}

// Function to validate radius
export function validateRadius(radius: number): boolean {
  if (!radius) return false;
  return coordinateRegex.test(radius.toString()) && radius > 0;
}

// Function to validate severity
export function validateSeverity(severity: number): boolean {
  if (severity === null || severity === undefined) return false;
  return (
    coordinateRegex.test(severity.toString()) && severity >= 0 && severity <= 5
  );
}

export function validatePassword(password: string): boolean {
  if (!password) return false;
  return passwordRegex.test(password);
}
