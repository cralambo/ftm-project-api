package org.ftm.api.board_member;

import java.time.LocalDate;

public class BoardMemberMapper {
    public static BoardMember mapToCreateRequest(BoardMemberRequest request) {
        return BoardMember.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .address(request.address())
                .personalEmail(request.personal_email())
                .professionalEmail(request.professional_email())
                .phone(request.phone())
                .position(request.position())
                .appointmentDate(LocalDate.parse(request.appointment_date()))
                .endDate(
                        request.end_date() != null
                                ? LocalDate.parse(request.end_date())
                                : null
                )
                .build();
    }

    public static BoardMemberResponse mapToBoardMemberResponse(BoardMember boardMember) {
        return BoardMemberResponse.builder()
                .id(boardMember.getId().toString())
                .firstname(boardMember.getFirstname())
                .lastname(boardMember.getLastname())
                .personal_email(boardMember.getPersonalEmail())
                .professional_email(boardMember.getProfessionalEmail())
                .position(boardMember.getPosition())
                .phone(boardMember.getPhone())
                .address(boardMember.getAddress())
                .appointment_date(
                        boardMember.getAppointmentDate() != null
                                ? boardMember.getAppointmentDate().toString()
                                : null
                )
                .end_date(
                        boardMember.getEndDate() != null
                                ? boardMember.getEndDate().toString()
                                : null
                )
                .build();
    }
}
