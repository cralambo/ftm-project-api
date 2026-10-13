package org.ftm.api.co_funder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoFunderRepository extends JpaRepository<CoFunder, UUID> {
}
