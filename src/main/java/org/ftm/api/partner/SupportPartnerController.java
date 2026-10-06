package org.ftm.api.partner;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class SupportPartnerController {

    private final SupportPartnerService supportPartnerService;

    @PostMapping("/create")
    public ResponseEntity<String> createPartner(@RequestBody SupportPartnerRequest request) {
        return ResponseEntity.ok(supportPartnerService.createPartner(request));
    }

    @GetMapping("/{partner-id}")
    public ResponseEntity<SupportPartnerResponse> getPartnerById(@PathVariable("partner-id") UUID partnerId) {
        return ResponseEntity.ok(supportPartnerService.getPartnerById(partnerId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SupportPartnerResponse>> getPartners(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(supportPartnerService.getPartners(page, size));
    }

    @PutMapping("/update/{partner-id}")
    public ResponseEntity<String> updatePartner(
            @PathVariable("partner-id") UUID partnerId,
            @RequestBody SupportPartnerRequest request
    ) {
        return ResponseEntity.ok(supportPartnerService.updatePartner(partnerId, request));
    }

    @DeleteMapping("/{partner-id}")
    public ResponseEntity<Void> deletePartner(@PathVariable("partner-id") UUID partnerId) {
        supportPartnerService.deletePartner(partnerId);
        return ResponseEntity.noContent().build();
    }
}
