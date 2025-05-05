import { defineStore } from "pinia";
import { ref } from "vue";
import type { PointOfInterest } from "@/types/PointOfInterest";

export const usePointStore = defineStore("pointStore", () => {
  const allPoints = ref<PointOfInterest[]>([]);
  const shelters = ref<PointOfInterest[]>([]);

  const fetchAllPoints = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/interest");
      if (!response.ok) throw new Error("Failed fetching all points");
      allPoints.value = await response.json();
    } catch (error) {
      console.error("Error fetching all points:", error);
      allPoints.value = [];
    }
  };

  const fetchNearestShelters = async (lat: number, lon: number) => {
    try {
      // TODO: Call backend
      // if (!response.ok) throw new Error("Failed fetching all points");
      // allPoints.value = await response.json();
    } catch (error) {
      console.error("Error fetching nearest shelters:", error);
      allPoints.value = [];
    }
  };

  const createPoint = async (point: PointOfInterest) => {
    try {
      const response = await fetch("http://localhost:8080/api/interest", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: point.name,
          iconType: point.iconType,
          description: point.description,
          latitude: point.latitude,
          longitude: point.longitude,
        }),
      });

      if (!response.ok) throw new Error("Failed to create point");

      const newPoint = await response.json();
      allPoints.value.push(newPoint);
      return newPoint;
    } catch (error) {
      console.error("Error creating point:", error);
      throw error;
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

  const deletePointById = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/interest/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) throw new Error("Failed to delete point");
      allPoints.value = allPoints.value.filter((point) => point.id !== id);
    } catch (error) {
      console.error("Error deleting point:", error);
      throw error;
    }
  };

  return {
    allPoints,
    shelters,
    fetchAllPoints,
    fetchNearestShelters,
    createPoint,
    updatePointById,
    deletePointById,
  };
});
