import { defineStore } from "pinia";
import type { EventRequestDTO, EventResponseDTO } from "@/types/Event";
import { EventService } from "@/api/EventService";
import { PollingService } from "@/services/PollingService";

const pollingService = new PollingService();

export const useEventStore = defineStore("events", {
  state: () => ({
    events: [] as EventResponseDTO[],
    activeEvents: [] as EventResponseDTO[],
    chosenEvent: {} as EventResponseDTO,
  }),

  actions: {
    startPollingActiveEvents() {
      this.fetchActiveEvents();
      pollingService.start(() => this.fetchActiveEvents());
    },
    stopPollingActiveEvents() {
      pollingService.stop();
    },
    async fetchEvents() {
      try {
        await EventService.findAll().then((data) => {
          this.events = data;
        });
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
    async update(id: number, event: EventRequestDTO) {
      try {
        await EventService.update(id, event).then((data) => {
          this.chosenEvent = data;
          this.fetchEvents();
        });
      } catch (error) {
        console.error("Error updating event:", error);
      }
    },
    async save(event: EventRequestDTO) {
      try {
        await EventService.save(event).then((data) => {
          this.chosenEvent = data;
          this.fetchEvents();
        });
      } catch (error) {
        console.error("Error saving event:", error);
      }
    },
    async delete(id: number) {
      try {
        await EventService.delete(id).then(() => {
          this.chosenEvent = {} as EventResponseDTO;
          this.fetchEvents();
          console.log(this.chosenEvent.id);
        });
      } catch (error) {
        console.error("Error deleting event:", error);
      }
    },
    clearEvents() {
      this.events = [];
      this.activeEvents = [];
    },
  },
});
