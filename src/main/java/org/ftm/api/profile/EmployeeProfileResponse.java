package org.ftm.api.profile;

import lombok.Builder;
import org.ftm.api.department.ShortDepartmentResponse;
import org.ftm.api.position.ShortPositionResponse;

@Builder
public record EmployeeProfileResponse(
        String id,
        String firstname,
        String lastname,
        String employee_number,
        String acronym,
        String personal_email,
        String professional_email,
        String phone,
        String address,
        String hire_date,
        ShortDepartmentResponse department,
        ShortPositionResponse position
) {
}
