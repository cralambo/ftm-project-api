package org.ftm.api.organization;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class LocalOrganizationController {

    private final LocalOrganizationService organizationService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('organizations.create')")
    public ResponseEntity<String> createOrganization(@RequestBody LocalOrganizationRequest request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }

    @GetMapping("/{organization-id}")
    @PreAuthorize("hasAuthority('organizations.view')")
    public ResponseEntity<LocalOrganizationResponse> getOrganizationById(@PathVariable("organization-id") UUID organizationId) {
        return ResponseEntity.ok(organizationService.getOrganizationById(organizationId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('organizations.view')")
    public ResponseEntity<PageResponse<LocalOrganizationResponse>> getOrganizations(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(organizationService.getOrganizations(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('organizations.view')")
    public ResponseEntity<List<ShortLocalOrganizationResponse>> getAllLocalOrganizations() {
        return ResponseEntity.ok(organizationService.getAllLocalOrganizations());
    }

    @PutMapping("/update/{organization-id}")
    @PreAuthorize("hasAuthority('organizations.update')")
    public ResponseEntity<String> updateOrganization(
            @PathVariable("organization-id") UUID organizationId,
            @RequestBody LocalOrganizationRequest request
    ) {
        return ResponseEntity.ok(organizationService.updateOrganization(organizationId, request));
    }

    @DeleteMapping("/{organization-id}")
    @PreAuthorize("hasAuthority('organizations.delete')")
    public ResponseEntity<Void> deleteOrganization(@PathVariable("organization-id") UUID organizationId) {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }
}
