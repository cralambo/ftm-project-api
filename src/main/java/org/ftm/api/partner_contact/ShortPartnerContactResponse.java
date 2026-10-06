package org.ftm.api.partner_contact;


import lombok.Builder;

@Builder
public record ShortPartnerContactResponse(
        String partner_contact_id,
        String firstname,
        String lastname
) {
}
