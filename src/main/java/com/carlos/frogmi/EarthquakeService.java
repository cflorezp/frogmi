package com.carlos.frogmi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class EarthquakeService {
    @Autowired
    private FeatureRepository featureRepository;

    public void fetchAndPersistEarthquakeData() {
        // URL del feed de USGS
        String usgsUrl = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";

        // Utilizar RestTemplate para obtener los datos del feed
        RestTemplate restTemplate = new RestTemplate();
        EarthquakeFeed earthquakeFeed = restTemplate.getForObject(usgsUrl, EarthquakeFeed.class);

        // Verificar si se obtuvieron datos
        if (earthquakeFeed != null && earthquakeFeed.getFeatures() != null) {
            // Iterar sobre los features y persistirlos en la base de datos
            for (Feature feature : earthquakeFeed.getFeatures()) {
                // Validar que los campos requeridos no sean nulos y que cumplan con los rangos
                if (isValidFeature(feature)) {
                    // Verificar si el feature ya existe en la base de datos
                    if (featureRepository.findByExternalId(feature.getExternalId()) == null) {
                        // Persistir el nuevo feature
                        featureRepository.save(feature);
                    }
                }
            }
        }
    }

    private boolean isValidFeature(Feature feature) {
        return feature.getTitle() != null && feature.getUrl() != null && feature.getPlace() != null &&
                feature.getMagType() != null && feature.getLongitude() != null && feature.getLatitude() != null &&
                feature.getMagnitude().compareTo(BigDecimal.valueOf(-1)) >= 0 && feature.getMagnitude().compareTo(BigDecimal.valueOf(10)) <= 0 &&
                feature.getLatitude() >= -90 && feature.getLatitude() <= 90 &&
                feature.getLongitude() >= -180 && feature.getLongitude() <= 180;
    }
}
