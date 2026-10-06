package org.ftm.api.department;

import lombok.Builder;

@Builder
public record DepartmentResponse(
        String department_id,
        String code,
        String name,
        String description
) {
}
