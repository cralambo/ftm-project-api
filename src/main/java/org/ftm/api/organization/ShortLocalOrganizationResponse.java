package org.ftm.api.organization;

import lombok.Builder;

@Builder
public record ShortLocalOrganizationResponse(
        String id,
        String name
) {
}
