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

  it("view mode renders correctly (without input fields or edit buttons)", () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "view",
      },
    });

    // Title = Points name
    expect(wrapper.find("h1").text()).toBe(testPoint.name);

    // Contain fields for reading
    expect(wrapper.text()).toContain("Type punkt:");
    expect(wrapper.text()).toContain("Beskrivelse:");
    expect(wrapper.text()).toContain("Koordinater:");
    expect(wrapper.text()).toContain("Naviger til dette punktet");

    // Not contain input fields or edit/create/delete buttons
    expect(wrapper.find("input").exists()).toBe(false);
    expect(wrapper.find("select").exists()).toBe(false);
    expect(wrapper.text()).not.toContain("Lag nytt punkt");
    expect(wrapper.text()).not.toContain("Lagre punkt");
    expect(wrapper.text()).not.toContain("Slett");
  });

  it('calls createPoint when clicking on "Lag nytt punkt" button', async () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "create",
      },
    });

    await wrapper.find("button.button").trigger("click");
    expect(pointStoreMock.createPoint).toHaveBeenCalled();
  });

  it('calls updatePointById when clicking on "Lagre punkt" button', async () => {
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

  it('confirms deletion and calls deletePointById when clicking "Slett"', async () => {
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

  it("emits close when clicking on close icon", async () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: testPoint,
        mode: "edit",
      },
    });

    await wrapper.find(".close-icon").trigger("click");
    expect(wrapper.emitted()).toHaveProperty("close");
  });

  it("disables create button when validation fails", async () => {
    const wrapper = mount(PointForm, {
      props: {
        selectedPoint: { ...testPoint, name: "" }, // Invalid name
        mode: "create",
      },
    });

    const createButton = wrapper.find("button.button");
    expect(createButton.attributes("disabled")).toBeDefined();
  });
});
