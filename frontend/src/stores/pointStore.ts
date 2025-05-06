import type { PointOfInterest } from "@/types/PointOfInterest";
import { defineStore } from "pinia";
import { ref } from "vue";
import { PointService } from "@/api/PointService";

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

  const fetchPointsByIconTypes = async (iconTypes: string[]) => {
    const points = await PointService.getByIconTypes(iconTypes);
    pointsDisplaying.value = points;
  };

  const fetchNearestShelters = async (latitude: number, longitude: number) => {
    return await PointService.getNearestShelters(latitude, longitude);
  };

  const createPoint = async (point: PointOfInterest) => {
    try {
      const newPoint = await PointService.save(point);
      fetchPointsByIconTypes(selectedIcons.value);
      return newPoint;
    } catch (error) {
      console.error("Error creating point:", error);
      throw error;
    }
  };

  const updatePointById = async (point: PointOfInterest) => {
    try {
      const updated = await PointService.update(point.id, point);
      fetchPointsByIconTypes(selectedIcons.value);
      return updated;
    } catch (error) {
      console.error("Error updating point:", error);
      throw error;
    }
  };

  const deletePointById = async (id: number) => {
    try {
      await PointService.remove(id);
      fetchPointsByIconTypes(selectedIcons.value);
    } catch (error) {
      console.error("Error deleting point:", error);
      throw error;
    }
  };

  return {
    pointsDisplaying,
    selectedIcons,
    startPolling,
    stopPolling,
    initializePolling,
    fetchPointsByIconTypes,
    fetchNearestShelters,
    createPoint,
    updatePointById,
    deletePointById,
    updateSelectedIcons,
  };
});
