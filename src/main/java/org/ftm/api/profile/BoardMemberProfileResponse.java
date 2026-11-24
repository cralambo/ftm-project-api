package org.ftm.api.profile;

import lombok.Builder;

@Builder
public record BoardMemberProfileResponse(
        String id,
        String firstname,
        String lastname,
        String personal_email,
        String professional_email,
        String phone,
        String address,
        String position,
        String appointment_date,
        String end_date
) {
}
