package org.ftm.api.partner_contact;

import lombok.Builder;
import org.ftm.api.partner.ShortSupportPartnerResponse;

@Builder
public record PartnerContactResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        ShortSupportPartnerResponse supportPartner
) {
}
