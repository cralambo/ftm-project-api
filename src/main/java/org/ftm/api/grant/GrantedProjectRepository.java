package org.ftm.api.grant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GrantedProjectRepository extends JpaRepository<GrantedProject, UUID> {
}
