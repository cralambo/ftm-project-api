package org.ftm.api.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupportPartnerRepository extends JpaRepository<SupportPartner, UUID> {
}
