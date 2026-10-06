package org.ftm.api.partner;

import lombok.Builder;

@Builder
public record SupportPartnerRequest(
        String name,
        String address,
        String phone,
        String email
) {
}
