package org.ftm.api.employee;

import lombok.Builder;

@Builder
public record EmployeeRequest(
        String firstname,
        String lastname,
        String employee_number,
        String acronym,
        String personal_email,
        String professional_email,
        String phone,
        String address,
        String hire_date,
        String position_id,
        String department_id
) {
}
