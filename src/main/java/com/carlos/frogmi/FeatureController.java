package com.carlos.frogmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/features")
public class FeatureController {
    @Autowired
    private FeatureRepository featureRepository;

    @GetMapping
    public ResponseEntity<Page<Feature>> getFeatures(@RequestParam(required = false) String[] mag_type,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "100") int per_page) {

        Pageable pageable = PageRequest.of(page - 1, per_page, Sort.by("id").descending());

        // Obtener los features según los filtros (mag_type)
        Page<Feature> featurePage;
        if (mag_type != null && mag_type.length > 0) {
            featurePage = featureRepository.findByMagTypeIn(mag_type, pageable);
        } else {
            featurePage = featureRepository.findAll(pageable);
        }
        return ResponseEntity.ok(featurePage);
    }


    @PostMapping("/{featureId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long featureId, @RequestBody Map<String, String> requestBody) {
        // Verificar si el cuerpo de la solicitud contiene el comentario
        if (requestBody.containsKey("comment")) {
            String comment = requestBody.get("comment");


            return ResponseEntity.ok().body("Comentario creado exitosamente para el feature con ID: " + featureId);
        } else {
            // Si el cuerpo de la solicitud no contiene el comentario, devolvemos un mensaje de error
            return ResponseEntity.badRequest().body("El cuerpo de la solicitud debe contener el comentario");
        }
    }

}
