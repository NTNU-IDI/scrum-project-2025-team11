package no.ntnu.idatt2106.krisefikser.importer;

import com.fasterxml.jackson.databind.JsonNode;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import no.ntnu.idatt2106.krisefikser.model.Enums.IconEnum;
import no.ntnu.idatt2106.krisefikser.repository.PointOfInterestRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShelterImporter {
    @Bean
    ApplicationRunner importShelters(GeoJsonImporter importer,
                                     PointOfInterestRepository repo) {
        return args -> importer.importFrom(
            "geojson/shelters.geojson",
            feature -> {
                JsonNode props    = feature.get("properties");
                JsonNode geom     = feature.get("geometry").get("coordinates");
                PointOfInterest p = new PointOfInterest();
                p.setName(props.get("adresse").asText());
                p.setLatitude(geom.get(1).asDouble());
                p.setLongitude(geom.get(0).asDouble());
                p.setIconType(IconEnum.shelter);
                p.setDescription("plasser: " + props.get("plasser").asInt());
                return p;
            },
            (location, poi) -> repo.save(poi)
        );
    }
}
