package org.ftm.api.project;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProjectResponse>> getProjects(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(projectService.getProjects(page, size));
    }

    @GetMapping("/{project-id}")
    public ResponseEntity<ProjectDetailResponse> getProjectByID(@PathVariable("project-id") UUID projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/partner")
    public ResponseEntity<PageResponse<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        UUID userId = principal.getId();
        return ResponseEntity.ok(projectService.getMyProjects(userId, page, size));
    }
}
