package org.ftm.api.partner;

import lombok.Builder;

@Builder
public record ShortSupportPartnerResponse(
        String support_partner_id,
        String name
) {
}
