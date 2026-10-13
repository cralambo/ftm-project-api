package org.ftm.api.partner;

import lombok.Builder;

@Builder
public record ShortSupportPartnerResponse(
        String id,
        String name
) {
}
