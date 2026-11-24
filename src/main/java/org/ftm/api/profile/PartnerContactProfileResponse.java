package org.ftm.api.profile;

import lombok.Builder;
import org.ftm.api.partner.ShortSupportPartnerResponse;

@Builder
public record PartnerContactProfileResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        ShortSupportPartnerResponse supportPartner
) {
}
