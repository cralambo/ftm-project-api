package org.ftm.api.district;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {
    boolean existsByCode(String code);
    List<District> findByRegionId(UUID regionId);
}
