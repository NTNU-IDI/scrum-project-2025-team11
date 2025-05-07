import axios from "axios";
import type { PointOfInterest } from "@/types/PointOfInterest";

const POINT_API_URL = "http://localhost:8080/api/interest";

export class PointService {
  static async list(): Promise<PointOfInterest[]> {
    const response = await axios.get(POINT_API_URL);
    return response.data;
  }

  static async getByIconTypes(iconTypes: string[]): Promise<PointOfInterest[]> {
    if (!iconTypes.length) return [];
    try {
      const query = iconTypes
        .map((type) => `iconType=${encodeURIComponent(type)}`)
        .join("&");
      const response = await axios.get(`${POINT_API_URL}/iconTypes?${query}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching filtered points:", error);
      return [];
    }
  }

  static async getNearestShelters(
    latitude: number,
    longitude: number
  ): Promise<PointOfInterest[]> {
    try {
      const response = await axios.get(`${POINT_API_URL}/closestShelters`, {
        params: { latitude, longitude },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching nearest shelters:", error);
      return [];
    }
  }

  static async save(point: PointOfInterest): Promise<PointOfInterest> {
    const response = await axios.post(POINT_API_URL, point);
    return response.data;
  }

  static async update(
    id: number,
    point: PointOfInterest
  ): Promise<PointOfInterest> {
    const response = await axios.put(`${POINT_API_URL}/${id}`, point);
    return response.data;
  }

  static async remove(id: number): Promise<void> {
    await axios.delete(`${POINT_API_URL}/${id}`);
  }
}
