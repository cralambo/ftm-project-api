package org.ftm.api.landscape;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LandscapeRepository extends JpaRepository<Landscape, UUID> {
}
