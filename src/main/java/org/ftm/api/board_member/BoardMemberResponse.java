package org.ftm.api.board_member;

import lombok.Builder;

@Builder
public record BoardMemberResponse(
        String board_member_id,
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
