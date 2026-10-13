package org.ftm.api.partner_contact;


import lombok.Builder;

@Builder
public record ShortPartnerContactResponse(
        String id,
        String firstname,
        String lastname
) {
}
