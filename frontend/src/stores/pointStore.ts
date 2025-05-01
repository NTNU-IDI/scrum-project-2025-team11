import { defineStore } from "pinia";
import { ref } from "vue";

export interface PointOfInterest {
  id: number;
  name: string;
  iconType: string;
  description: string;
  latitude: number;
  longitude: number;
}

export const usePointStore = defineStore("pointStore", () => {
  const allPoints = ref<PointOfInterest[]>([]);
  const shelters = ref<PointOfInterest[]>([]);

  const fetchAllPoints = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/interest");
      if (!response.ok) throw new Error("Failed fetching al points");
      allPoints.value = await response.json();
    } catch (error) {
      console.error("Error fetching all points:", error);
      allPoints.value = [];
    }
  };

  const fetchShelters = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/interest/iconType?iconType=shelter"
      );
      if (!response.ok) throw new Error("Failed fetching shelters");
      shelters.value = await response.json();
    } catch (error) {
      console.error("Error fetching shelters:", error);
      shelters.value = [];
    }
  };

  const updatePointById = async (point: PointOfInterest) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/interest/${point.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(point),
        }
      );

      if (!response.ok) throw new Error("Failed to update point");

      const updated = await response.json();
      await fetchAllPoints();
      return updated;
    } catch (error) {
      console.error("Error updating point:", error);
      throw error;
    }
  };

  return {
    allPoints,
    shelters,
    fetchAllPoints,
    fetchShelters,
    updatePointById,
  };
});
