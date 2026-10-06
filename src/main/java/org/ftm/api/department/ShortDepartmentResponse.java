package org.ftm.api.department;

import lombok.Builder;

@Builder
public record ShortDepartmentResponse(
        String department_id,
        String code,
        String name
) {
}
