package org.ftm.api.partner;


import lombok.Builder;

@Builder
public record SupportPartnerResponse(
        String id,
        String name,
        String address,
        String phone,
        String email
) {
}
