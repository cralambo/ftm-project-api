package org.ftm.api.organization;

import lombok.Builder;


@Builder
public record LocalOrganizationRequest(
        String name,
        String address,
        String phone,
        String email
) {
}
