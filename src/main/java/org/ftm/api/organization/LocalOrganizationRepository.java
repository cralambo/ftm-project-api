package org.ftm.api.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocalOrganizationRepository extends JpaRepository<LocalOrganization, UUID> {
}
