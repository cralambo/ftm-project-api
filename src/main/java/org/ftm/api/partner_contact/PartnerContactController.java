package org.ftm.api.partner_contact;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/partner-contacts")
public class PartnerContactController {

    private final PartnerContactService partnerContactService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('partner_contacts.create')")
    public ResponseEntity<String> createPartnerContact(@RequestBody PartnerContactRequest request) {
        return ResponseEntity.ok(partnerContactService.createPartnerContact(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('partner_contacts.view')")
    public ResponseEntity<PageResponse<PartnerContactResponse>> getPartnerContacts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(partnerContactService.getPartnerContacts(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('partner_contacts.view')")
    public ResponseEntity<List<ShortPartnerContactResponse>> getAllPartnerContacts() {
        return ResponseEntity.ok(partnerContactService.getAllPartnerContacts());
    }

    @GetMapping("/{partner-contact-id}")
    @PreAuthorize("hasAuthority('partner_contacts.view')")
    public ResponseEntity<PartnerContactResponse> getPartnerContactById(@PathVariable("partner-contact-id") UUID partnerContactId) {
        return ResponseEntity.ok(partnerContactService.getPartnerContactById(partnerContactId));
    }

    @PutMapping("/update/{partner-contact-id}")
    @PreAuthorize("hasAuthority('partner_contacts.update')")
    public ResponseEntity<String> updatePartnerContact(
            @PathVariable("partner-contact-id") UUID partnerContactId,
            @RequestBody PartnerContactRequest request
    ) {
        return ResponseEntity.ok(partnerContactService.updatePartnerContact(partnerContactId, request));
    }

    @DeleteMapping("/{partner-contact-id}")
    @PreAuthorize("hasAuthority('partner_contacts.delete')")
    public ResponseEntity<Void> deletePartnerContact(@PathVariable("partner-contact-id") UUID partnerContactId) {
        partnerContactService.deletePartnerContact(partnerContactId);
        return ResponseEntity.noContent().build();
    }
 }
