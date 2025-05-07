export class PollingService {
  private intervalId: ReturnType<typeof setInterval> | null = null;

  start(callback: () => void, interval = 5000) {
    if (this.intervalId) return;
    this.intervalId = setInterval(callback, interval);
  }

  stop() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = null;
    }
  }

  isRunning() {
    return this.intervalId !== null;
  }
}
