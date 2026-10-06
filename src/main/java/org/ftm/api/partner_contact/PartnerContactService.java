package org.ftm.api.partner_contact;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.partner.SupportPartner;
import org.ftm.api.partner.SupportPartnerRepository;
import org.ftm.api.user.User;
import org.ftm.api.user.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerContactService {

    private final PartnerContactRepository partnerContactRepository;
    private final SupportPartnerRepository supportPartnerRepository;
    private final UserAccountService userAccountService;

    @Transactional
    public String createPartnerContact(PartnerContactRequest request) {
        PartnerContact partnerContact = PartnerContactMapper.mapToCreatePartnerContact(request);

        final SupportPartner supportPartner = supportPartnerRepository.findById(UUID.fromString(request.support_partner_id()))
                .orElseThrow(() -> new EntityNotFoundException("No support partner found with id: " + request.support_partner_id()));

        partnerContact.setSupportPartner(supportPartner);

        // create user account
        final User user = userAccountService.createPartnerContactAccount(
                request.email(),
                request.firstname(),
                request.lastname()
        );

        partnerContact.setUser(user);

        return partnerContactRepository.save(partnerContact).getId().toString();
    }

    public PageResponse<PartnerContactResponse> getPartnerContacts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PartnerContact> partnerContacts = partnerContactRepository.findAll(pageable);
        List<PartnerContactResponse> content = partnerContacts
                .stream()
                .map(PartnerContactMapper::mapToPartnerContactResponse)
                .toList();
        return PageResponse.<PartnerContactResponse>builder()
                .content(content)
                .number(partnerContacts.getNumber())
                .size(partnerContacts.getSize())
                .totalElements(partnerContacts.getTotalElements())
                .totalPages(partnerContacts.getTotalPages())
                .first(partnerContacts.isFirst())
                .last(partnerContacts.isLast())
                .build();
    }

    public List<ShortPartnerContactResponse> getAllPartnerContacts() {
        List<PartnerContact> partnerContacts = partnerContactRepository.findAll(Sort.by("createdDate").descending());
        return partnerContacts
                .stream()
                .map(PartnerContactMapper::mapToShortPartnerContactResponse)
                .toList();
    }

    public PartnerContactResponse getPartnerContactById(UUID partnerContactId) {
        return partnerContactRepository.findById(partnerContactId)
                .map(PartnerContactMapper::mapToPartnerContactResponse)
                .orElseThrow(() -> new EntityNotFoundException("No partner contact found with id: " + partnerContactId));
    }

    public String updatePartnerContact(UUID partnerContactId, PartnerContactRequest request) {
        PartnerContact partnerContact = partnerContactRepository.findById(partnerContactId)
                .orElseThrow(() -> new EntityNotFoundException("No partner contact found with id: " + partnerContactId));

        final SupportPartner supportPartner = supportPartnerRepository.findById(UUID.fromString(request.support_partner_id()))
                .orElseThrow(() -> new EntityNotFoundException("No support partner found with id: " + request.support_partner_id()));

        partnerContact.setFirstname(request.firstname());
        partnerContact.setLastname(request.lastname());
        partnerContact.setEmail(request.email());
        partnerContact.setPhone(request.phone());
        partnerContact.setSupportPartner(supportPartner);

        return partnerContactRepository.save(partnerContact).getId().toString();
    }

    public void deletePartnerContact(UUID partnerContactId) {
        final PartnerContact partnerContact = partnerContactRepository.findById(partnerContactId)
                .orElseThrow(() -> new EntityNotFoundException("No partner contact found with id: " + partnerContactId));
        partnerContactRepository.delete(partnerContact);
    }
}
