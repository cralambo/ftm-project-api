package org.ftm.api.partner_contact;

import lombok.Builder;

@Builder
public record PartnerContactRequest(
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String support_partner_id
) {
}
