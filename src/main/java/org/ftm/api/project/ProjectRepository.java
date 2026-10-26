package org.ftm.api.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @EntityGraph(attributePaths = {
            "supportPartner",
            "grantedProject"
    })
    @Query("""
        SELECT p
        FROM Project p
        ORDER BY p.createdDate DESC
        """)
    Page<Project> findAllWithGrantedProjectAndSupportPartner(Pageable pageable);

    @EntityGraph(attributePaths = {
            "region",
            "district",
            "municipality",
            "landscape",
            "subLandscape",
            "supportPartner",
            "localOrganization",
            "strategicPlan",
            "topics",
            "projectCoFunders",
            "projectCoFunders.coFunder",
            "disbursementSchedules",
            "grantedProject"
    })
    @Query("""
        SELECT DISTINCT p
        FROM Project p
        WHERE p.id = :id
        """)
    Optional<Project> findByIdWithAllRelations(@Param("id") UUID id);

    @EntityGraph(attributePaths = {
            "supportPartner",
            "grantedProject"
    })
    @Query("""
        SELECT p
        FROM Project p
        WHERE p.supportPartner.id = :supportPartnerId
        ORDER BY p.createdDate DESC
        """)
    Page<Project> findBySupportPartnerId(
            @Param("supportPartnerId") UUID supportPartnerId,
            Pageable pageable
    );
}
