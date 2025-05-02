import { mount } from "@vue/test-utils";
import PointForm from "@/components/map/PointForm.vue";
import { createPinia, setActivePinia } from "pinia";
import { describe, beforeEach, it, expect, vi } from "vitest";
import { usePointStore } from "@/stores/pointStore";

vi.mock("@/stores/pointStore");

describe("PointForm.vue", () => {
  let pointStoreMock: any;

  beforeEach(() => {
    setActivePinia(createPinia());
    pointStoreMock = {
      createPoint: vi.fn(),
      updatePointById: vi.fn(),
      deletePointById: vi.fn(),
    };
    (usePointStore as any).mockReturnValue(pointStoreMock);
  });

  const testPoint = {
    id: 1,
    name: "Test Point",
    description: "Test description",
    iconType: "shelter",
    latitude: 10.0,
    longitude: 20.0,
  };

  it("create mode renders correctly", () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "create",
      },
    });

    expect(wrapper.find("h1").text()).toBe("Nytt punkt");
    expect(wrapper.find("button.button").text()).toBe("Lag nytt punkt");
  });

  it("edit mode renders correctly", () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "edit",
      },
    });

    expect(wrapper.find("h1").text()).toBe("Endre punkt");
    expect(wrapper.text()).toContain("Lagre punkt");
    expect(wrapper.text()).toContain("Slett");
  });

  it('call createPoint when clicking on "Lag nytt punkt" button', async () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "create",
      },
    });

    await wrapper.find("button.button").trigger("click");
    expect(pointStoreMock.createPoint).toHaveBeenCalled();
  });

  it('call updatePointById when clicking on "Lagre punkt" button', async () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "edit",
      },
    });

    const saveButton = wrapper
      .findAll("button.button")
      .find((b) => b.text() === "Lagre punkt");
    await saveButton?.trigger("click");
    expect(pointStoreMock.updatePointById).toHaveBeenCalled();
  });

  it('confirms deletion and call deletePointById clciking on "Slett"" button', async () => {
    vi.spyOn(window, "confirm").mockReturnValue(true);

    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "edit",
      },
    });

    const deleteButton = wrapper
      .findAll("button")
      .find((b) => b.text() === "Slett");
    await deleteButton?.trigger("click");

    expect(window.confirm).toHaveBeenCalled();
    expect(pointStoreMock.deletePointById).toHaveBeenCalledWith(testPoint.id);
  });

  it("emit close when clicking on close icon", async () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "edit",
      },
    });

    await wrapper.find(".close-icon").trigger("click");
    expect(wrapper.emitted()).toHaveProperty("close");
  });
});
