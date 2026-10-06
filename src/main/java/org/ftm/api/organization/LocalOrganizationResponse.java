package org.ftm.api.organization;

import lombok.Builder;

@Builder
public record LocalOrganizationResponse(
        String local_organization_id,
        String name,
        String address,
        String phone,
        String email
) {
}
