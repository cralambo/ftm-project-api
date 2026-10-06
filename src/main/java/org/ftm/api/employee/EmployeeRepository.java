package org.ftm.api.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByProfessionalEmail(String professionalEmail);
    boolean existsByEmployeeNumber(String employeeNumber);
    boolean existsByAcronym(String acronym);
}
