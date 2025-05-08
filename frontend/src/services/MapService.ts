import L from "leaflet";
import type { PointOfInterest } from "@/types/PointOfInterest";

export function addMarkersToMap(
  map: L.Map,
  points: PointOfInterest[],
  markers: L.Marker[],
  role: string,
  isEditMode: boolean,
  showPointView: Function
) {
  markers.forEach((marker) => map.removeLayer(marker));
  markers.length = 0;

  points.forEach((point) => {
    const customIcon = L.divIcon({
      html: `<div class="map-icon ${point.iconType}" style="margin: 0;"></div>`,
      className: "",
      iconSize: [20, 20],
      iconAnchor: [10, 10],
    });

    const marker = L.marker([point.latitude, point.longitude], {
      icon: customIcon,
    }).addTo(map);
    markers.push(marker);

    marker.on("click", () => {
      if (role === "admin" && isEditMode) {
        showPointView("edit", point, true);
      } else {
        showPointView("view", point, true);
      }
    });
  });
}
