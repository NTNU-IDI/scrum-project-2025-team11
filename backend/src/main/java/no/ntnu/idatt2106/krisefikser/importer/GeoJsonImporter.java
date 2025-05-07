package no.ntnu.idatt2106.krisefikser.importer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class GeoJsonImporter {
    private final ObjectMapper mapper;

    public GeoJsonImporter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> void importFrom(
        String classpathLocation,
        Function<JsonNode, T> rowMapper,
        BiConsumer<String, T> persister
    ) {
        try (InputStream in = new ClassPathResource(classpathLocation).getInputStream()) {
            JsonNode root     = mapper.readTree(in);
            JsonNode features = root.get("features");
            if (features == null || !features.isArray()) {
                throw new IllegalArgumentException("Invalid GeoJSON: no features array");
            }
            for (JsonNode feature : features) {
                T record = rowMapper.apply(feature);
                persister.accept(classpathLocation, record);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to import GeoJSON from " + classpathLocation, e);
        }
    }
}
