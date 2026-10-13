package org.ftm.api.organization;

import lombok.Builder;

@Builder
public record LocalOrganizationResponse(
        String id,
        String name,
        String address,
        String phone,
        String email
) {
}
