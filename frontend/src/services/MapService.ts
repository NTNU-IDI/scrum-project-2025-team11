import L from "leaflet";
import userMarkerIcon from "@/assets/ikon/user-maker.svg";
import "leaflet-routing-machine";
import type { PointOfInterest } from "@/types/PointOfInterest";
import type { EventResponseDTO } from "@/types/Event";
import { getEventColor } from "@/utils/geoService";
import { type Ref } from "vue";

export const userIcon = L.icon({
  iconUrl: userMarkerIcon,
  iconSize: [35, 35],
  iconAnchor: [20, 40],
  popupAnchor: [0, -40],
});

export function addMarkersToMap(
  map: L.Map,
  points: PointOfInterest[],
  markers: L.Marker[],
  role: Ref<string>,
  isEditMode: Ref<boolean>,
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
      if (role.value === "admin" && isEditMode.value) {
        showPointView("edit", point, true);
      } else {
        showPointView("view", point, true);
      }
    });
  });
}

export function addEventsToMap(
  map: L.Map,
  activeEvents: EventResponseDTO[],
  eventLayers: L.Circle[],
  handleEventClick: (event: EventResponseDTO) => void
) {
  clearEventLayers(eventLayers, map);

  activeEvents.forEach((event) => {
    const color = getEventColor(event.severity);
    const circle = L.circle([event.latitude, event.longitude], {
      color,
      fillColor: color,
      weight: 1,
      radius: event.radius,
      fillOpacity: 0.3,
      interactive: true,
    }).addTo(map);

    circle.on("click", () => {
      handleEventClick(event);
    });

    eventLayers.push(circle);
  });
}

export function clearEventLayers(eventLayers: L.Circle[], map: L.Map) {
  eventLayers.forEach((layer) => map.removeLayer(layer));
  eventLayers.length = 0;
}

export function createRoutingControl(
  map: L.Map,
  startLat: number,
  startLon: number,
  endLat: number,
  endLon: number
) {
  const routingControl = L.Routing.control({
    waypoints: [L.latLng(startLat, startLon), L.latLng(endLat, endLon)],
    routeWhileDragging: false,
    lineOptions: {
      styles: [{ color: "var(--navigation)", weight: 6 }],
      extendToWaypoints: false,
      missingRouteTolerance: 0,
    },
    createMarker: function (i: number, waypoint: any) {
      if (i === 0) {
        return L.marker(waypoint.latLng, { icon: userIcon });
      }
      return L.marker(waypoint.latLng);
    },
    draggableWaypoints: false,
  } as any).addTo(map);
  return routingControl;
}

export function clearRoutingControl(map: L.Map, routingControl: any) {
  if (routingControl) {
    map.removeControl(routingControl);
  }
}
