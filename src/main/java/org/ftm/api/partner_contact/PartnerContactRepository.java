package org.ftm.api.partner_contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerContactRepository extends JpaRepository<PartnerContact, UUID> {
    Optional<PartnerContact> findByUserId(UUID userId);
}
