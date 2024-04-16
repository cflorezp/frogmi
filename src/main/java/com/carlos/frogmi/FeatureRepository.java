package com.carlos.frogmi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    Feature findByExternalId(String externalId);

    Page<Feature> findByMagTypeIn(String[] magTypes, Pageable pageable);
}
