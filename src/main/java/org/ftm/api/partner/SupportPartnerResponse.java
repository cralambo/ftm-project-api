package org.ftm.api.partner;


import lombok.Builder;

@Builder
public record SupportPartnerResponse(
        String support_partner_id,
        String name,
        String address,
        String phone,
        String email
) {
}
