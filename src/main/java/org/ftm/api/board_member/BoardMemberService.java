package org.ftm.api.board_member;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.user.User;
import org.ftm.api.user.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardMemberService {

    private final BoardMemberRepository boardMemberRepository;
    private final UserAccountService userAccountService;

    @Transactional
    public String createBoardMember(BoardMemberRequest request) {
        BoardMember boardMember = BoardMemberMapper.mapToCreateRequest(request);

        // create user account
        final User user = userAccountService.createBoardMemberAccount(
                request.professional_email(),
                request.firstname(),
                request.lastname()
        );

        boardMember.setUser(user);

        return boardMemberRepository.save(boardMember).getId().toString();
    }

    public BoardMemberResponse getBoardMemberById(UUID boardMemberId) {
        return boardMemberRepository.findById(boardMemberId)
                .map(BoardMemberMapper::mapToBoardMemberResponse)
                .orElseThrow(() -> new EntityNotFoundException("No board member found with id: " + boardMemberId));
    }

    public PageResponse<BoardMemberResponse> getBoardMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BoardMember> boardMembers = boardMemberRepository.findAll(pageable);
        List<BoardMemberResponse> content = boardMembers.stream()
                .map(BoardMemberMapper::mapToBoardMemberResponse)
                .toList();
        return PageResponse.<BoardMemberResponse>builder()
                .content(content)
                .number(boardMembers.getNumber())
                .size(boardMembers.getSize())
                .totalElements(boardMembers.getTotalElements())
                .totalPages(boardMembers.getTotalPages())
                .first(boardMembers.isFirst())
                .last(boardMembers.isLast())
                .build();
    }

    public String updateBoardMember(UUID boardMemberId, BoardMemberRequest request) {
        BoardMember boardMember = boardMemberRepository.findById(boardMemberId)
                .orElseThrow(() -> new EntityNotFoundException("No board member found with id: " + boardMemberId));

        boardMember.setFirstname(request.firstname());
        boardMember.setLastname(request.lastname());
        boardMember.setPersonalEmail(request.personal_email());
        boardMember.setProfessionalEmail(request.professional_email());
        boardMember.setPosition(request.position());
        boardMember.setPhone(request.phone());
        boardMember.setAddress(request.address());
        boardMember.setAppointmentDate(LocalDate.parse(request.appointment_date()));
        boardMember.setEndDate(LocalDate.parse(request.end_date()));

        return boardMemberRepository.save(boardMember).getId().toString();
    }

    public void deleteBoardMember(UUID boardMemberId) {
        final BoardMember boardMember = boardMemberRepository.findById(boardMemberId)
                .orElseThrow(() -> new EntityNotFoundException("No board member found with id: " + boardMemberId));
        boardMemberRepository.delete(boardMember);
    }
}
