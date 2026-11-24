package org.ftm.api.board_member;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/board-members")
@RequiredArgsConstructor
public class BoardMemberController {

    private final BoardMemberService boardMemberService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('board_members.create')")
    public ResponseEntity<String> createBoardMember(@RequestBody BoardMemberRequest request) {
        return ResponseEntity.ok(boardMemberService.createBoardMember(request));
    }

    @GetMapping("/{board-member-id}")
    @PreAuthorize("hasAuthority('board_members.view')")
    public ResponseEntity<BoardMemberResponse> getBoardMemberById(@PathVariable("board-member-id") UUID boardMemberId) {
        return ResponseEntity.ok(boardMemberService.getBoardMemberById(boardMemberId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('board_members.view')")
    public ResponseEntity<PageResponse<BoardMemberResponse>> getBoardMembers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(boardMemberService.getBoardMembers(page, size));
    }

    @PutMapping("/update/{board-member-id}")
    @PreAuthorize("hasAuthority('board_members.update')")
    public ResponseEntity<String> updateBoardMember(
            @PathVariable("board-member-id") UUID boardMemberId,
            @RequestBody BoardMemberRequest request
    ) {
        return ResponseEntity.ok(boardMemberService.updateBoardMember(boardMemberId, request));
    }

    @DeleteMapping("/{board-member-id}")
    @PreAuthorize("hasAuthority('board_members.delete')")
    public ResponseEntity<Void> deleteBoardMember(@PathVariable("board-member-id") UUID boardMemberId) {
        boardMemberService.deleteBoardMember(boardMemberId);
        return ResponseEntity.noContent().build();
    }
}
