import type { PointOfInterest } from "@/types/PointOfInterest";
import { defineStore } from "pinia";
import { ref } from "vue";
import { PointService } from "@/api/PointService";
import { useToast } from "vue-toast-notification";
import { PollingService } from "@/services/PollingService";

export const usePointStore = defineStore("pointStore", () => {
  const pointsDisplaying = ref<PointOfInterest[]>([]);
  const selectedIcons = ref<string[]>([]);
  const polling = new PollingService();

  const startPolling = () => {
    polling.start(() => fetchPointsByIconTypes(selectedIcons.value));
  };

  const stopPolling = () => {
    polling.stop();
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
    const $toast = useToast();
    try {
      const newPoint = await PointService.save(point);
      fetchPointsByIconTypes(selectedIcons.value);
      $toast.success("Punktet ble laget!", {
        duration: 5000,
      });
      return newPoint;
    } catch (error) {
      $toast.warning("Punktet kunne ikke bli lagd", { duration: 5000 });
      throw error;
    }
  };

  const updatePointById = async (point: PointOfInterest) => {
    const $toast = useToast();
    try {
      const updated = await PointService.update(point.id, point);
      fetchPointsByIconTypes(selectedIcons.value);
      $toast.success("Punktet ble oppdatert!", {
        duration: 5000,
      });
      return updated;
    } catch (error) {
      $toast.warning("Punktet kunne ikke bli oppdatert", { duration: 5000 });
      throw error;
    }
  };

  const deletePointById = async (id: number) => {
    const $toast = useToast();
    try {
      await PointService.remove(id);
      fetchPointsByIconTypes(selectedIcons.value);
      $toast.success("Punktet ble slettet!", {
        duration: 5000,
      });
    } catch (error) {
      $toast.warning("Punktet kunne ikke bli slettet", { duration: 5000 });
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
