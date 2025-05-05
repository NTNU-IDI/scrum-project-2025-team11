import { defineStore } from "pinia";
import type { EventResponseDTO } from "@/types/Event";
import { EventService } from "@/api/EventService";

export const useEventStore = defineStore("events", {
  state: () => ({
    events: [] as EventResponseDTO[],
    activeEvents: [] as EventResponseDTO[],
    chosenEvent: {} as EventResponseDTO,
  }),

  actions: {
    async fetchEvents() {
      try {
        await EventService.findAll().then((data) => {
          this.events = data;
        });
        console.log(this.events);
      } catch (error) {
        console.error("Error fetching event:", error);
      }
    },
    async fetchActiveEvents() {
      try {
        const data = await EventService.findActive();
        this.activeEvents = data;
        return this.activeEvents;
      } catch (error) {
        console.error("Error fetching active events:", error);
        this.activeEvents = [];
        return [];
      }
    },
    async fetchChosenEvent() {
      try {
        await EventService.findById(this.chosenEvent.id).then((data) => {
          this.chosenEvent = data;
        });
      } catch (error) {
        console.error("Error fetching chosen event:", error);
      }
    },
    async chooseEvent(id: number) {
      try {
        const event = this.events.find((event) => event.id === id);
        if (event) {
          this.chosenEvent = event;
        } else {
          console.error("Event not found");
        }
      } catch (error) {
        console.error("Error choosing event:", error);
      }
    },
    clearEvents() {
      this.events = [];
      this.activeEvents = [];
    },
  },
});
