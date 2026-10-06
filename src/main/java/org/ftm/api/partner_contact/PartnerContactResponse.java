package org.ftm.api.partner_contact;

import lombok.Builder;
import org.ftm.api.partner.ShortSupportPartnerResponse;

@Builder
public record PartnerContactResponse(
        String partner_contact_id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        ShortSupportPartnerResponse supportPartner
) {
}
