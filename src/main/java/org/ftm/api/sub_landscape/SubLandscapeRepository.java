package org.ftm.api.sub_landscape;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubLandscapeRepository extends JpaRepository<SubLandscape, UUID> {
    List<SubLandscape> findByLandscapeId(UUID landscapeId);
}
