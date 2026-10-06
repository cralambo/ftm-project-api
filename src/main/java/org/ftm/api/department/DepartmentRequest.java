package org.ftm.api.department;

import lombok.Builder;

@Builder
public record DepartmentRequest(
        String code,
        String name,
        String description
) {}
