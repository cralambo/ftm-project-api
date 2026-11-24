package org.ftm.api.partner;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class SupportPartnerController {

    private final SupportPartnerService supportPartnerService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('partners.create')")
    public ResponseEntity<String> createPartner(@RequestBody SupportPartnerRequest request) {
        return ResponseEntity.ok(supportPartnerService.createPartner(request));
    }

    @GetMapping("/{partner-id}")
    @PreAuthorize("hasAuthority('partners.view')")
    public ResponseEntity<SupportPartnerResponse> getPartnerById(@PathVariable("partner-id") UUID partnerId) {
        return ResponseEntity.ok(supportPartnerService.getPartnerById(partnerId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('partners.view')")
    public ResponseEntity<List<ShortSupportPartnerResponse>> getAllPartners() {
        return ResponseEntity.ok(supportPartnerService.getAllPartners());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('partners.view')")
    public ResponseEntity<PageResponse<SupportPartnerResponse>> getPartners(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(supportPartnerService.getPartners(page, size));
    }

    @PutMapping("/update/{partner-id}")
    @PreAuthorize("hasAuthority('partners.update')")
    public ResponseEntity<String> updatePartner(
            @PathVariable("partner-id") UUID partnerId,
            @RequestBody SupportPartnerRequest request
    ) {
        return ResponseEntity.ok(supportPartnerService.updatePartner(partnerId, request));
    }

    @DeleteMapping("/{partner-id}")
    @PreAuthorize("hasAuthority('partners.delete')")
    public ResponseEntity<Void> deletePartner(@PathVariable("partner-id") UUID partnerId) {
        supportPartnerService.deletePartner(partnerId);
        return ResponseEntity.noContent().build();
    }
}
