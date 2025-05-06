import type { PointOfInterest } from "@/types/PointOfInterest";
import { defineStore } from "pinia";
import { ref } from "vue";

// TODO: Move code to api/PointService.ts

export const usePointStore = defineStore("pointStore", () => {
  const pointsDisplaying = ref<PointOfInterest[]>([]);
  const selectedIcons = ref<string[]>([]);
  let pollingInterval: ReturnType<typeof setInterval> | null = null;

  const startPolling = () => {
    if (pollingInterval) return;
    pollingInterval = setInterval(() => {
      fetchPointsByIconTypes(selectedIcons.value);
    }, 5000); // 5 seconds
  };

  const stopPolling = () => {
    if (pollingInterval) {
      clearInterval(pollingInterval);
      pollingInterval = null;
    }
  };

  const initializePolling = async () => {
    await fetchPointsByIconTypes(selectedIcons.value);
    startPolling();
  };

  const updateSelectedIcons = (newIcons: string[]) => {
    selectedIcons.value = newIcons;
    stopPolling();
    fetchPointsByIconTypes(newIcons);
    startPolling();
  };

  const fetchAllPoints = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/interest");
      if (!response.ok) throw new Error("Failed fetching all points");
      pointsDisplaying.value = await response.json();
    } catch (error) {
      console.error("Error fetching all points:", error);
      pointsDisplaying.value = [];
    }
  };

  const fetchPointsByIconTypes = async (iconTypes: string[]) => {
    if (!iconTypes.length) {
      pointsDisplaying.value = [];
      return;
    }

    try {
      const query = iconTypes
        .map((type) => `iconType=${encodeURIComponent(type)}`)
        .join("&");
      const response = await fetch(
        `http://localhost:8080/api/interest/iconTypes?${query}`
      );
      if (!response.ok) throw new Error("Failed fetching filtered points");
      pointsDisplaying.value = await response.json();
    } catch (error) {
      console.error("Error fetching filtered points:", error);
      pointsDisplaying.value = [];
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
      pointsDisplaying.value.push(newPoint);
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
      pointsDisplaying.value.push(updated);
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
      pointsDisplaying.value = pointsDisplaying.value.filter(
        (point) => point.id !== id
      );
    } catch (error) {
      console.error("Error deleting point:", error);
      throw error;
    }
  };

  return {
    pointsDisplaying,
    startPolling,
    stopPolling,
    initializePolling,
    fetchAllPoints,
    fetchPointsByIconTypes,
    createPoint,
    updatePointById,
    deletePointById,
    updateSelectedIcons,
  };
});
