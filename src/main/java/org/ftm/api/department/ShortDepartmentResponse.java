package org.ftm.api.department;

import lombok.Builder;

@Builder
public record ShortDepartmentResponse(
        String id,
        String code,
        String name
) {
}
