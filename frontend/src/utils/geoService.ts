import type { EventResponseDTO } from "@/types/Event";

export function calculateDistance(
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number
): number {
  const R = 6371;
  const dLat = toRadians(lat2 - lat1);
  const dLon = toRadians(lon2 - lon1);

  // Haversine formula
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRadians(lat1)) *
      Math.cos(toRadians(lat2)) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);

  // Central angle
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return R * c * 1000;
}

export function toRadians(degrees: number): number {
  return degrees * (Math.PI / 180);
}

export function getEventColor(severity: number) {
  if (severity === 0 || severity === 1) {
    return "var(--yellow)";
  } else if (severity === 2 || severity === 3) {
    return "var(--light-orange)";
  } else if (severity === 4 || severity === 5) {
    return "var(--bad-red)";
  } else {
    return "var(--bad-red)";
  }
}

export function isUserInCrisisArea(
  lat: number,
  lon: number,
  activeEvents: EventResponseDTO[]
): boolean {
  return activeEvents.some(
    (event) =>
      calculateDistance(lat, lon, event.latitude, event.longitude) <=
      event.radius
  );
}

export async function resolveAddressFromText(
  address: string
): Promise<{ lat: number; lon: number } | null> {
  if (!address.trim()) return null;

  try {
    const response = await fetch(
      `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
        address
      )}`
    );
    const data = await response.json();

    if (data.length > 0) {
      return {
        lat: parseFloat(data[0].lat),
        lon: parseFloat(data[0].lon),
      };
    } else {
      return null;
    }
  } catch (err) {
    throw new Error("En feil skjedde ved oppslag av adresse.");
  }
}

export async function resolveAddressFromCoords(
  lat: number,
  lon: number
): Promise<string | null> {
  try {
    const response = await fetch(
      `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`
    );
    const data = await response.json();
    return data?.display_name || null;
  } catch (err) {
    throw new Error("En feil skjedde ved oppslag av koordinater.");
  }
}
