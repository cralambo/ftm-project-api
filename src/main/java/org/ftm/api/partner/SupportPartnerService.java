package org.ftm.api.partner;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportPartnerService {

    private final SupportPartnerRepository supportPartnerRepository;

    public String createPartner(SupportPartnerRequest request) {
        final SupportPartner partner = SupportPartnerMapper.mapToCreatePartner(request);
        return supportPartnerRepository.save(partner).getId().toString();
    }

    public SupportPartnerResponse getPartnerById(UUID partnerId) {
        return supportPartnerRepository.findById(partnerId)
                .map(SupportPartnerMapper::mapToPartnerResponse)
                .orElseThrow(() -> new EntityNotFoundException("No support partner found with id: " + partnerId));
    }

    public PageResponse<SupportPartnerResponse> getPartners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<SupportPartner> partners = supportPartnerRepository.findAll(pageable);
        List<SupportPartnerResponse> content = partners
                .stream()
                .map(SupportPartnerMapper::mapToPartnerResponse)
                .toList();
        return PageResponse.<SupportPartnerResponse>builder()
                .content(content)
                .number(partners.getNumber())
                .size(partners.getSize())
                .totalElements(partners.getTotalElements())
                .totalPages(partners.getTotalPages())
                .first(partners.isFirst())
                .last(partners.isLast())
                .build();
    }

    public String updatePartner(UUID partnerId, SupportPartnerRequest request) {
        SupportPartner partner = supportPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("No support partner found with id: " + partnerId));
        partner.setName(request.name());
        partner.setAddress(request.address());
        partner.setPhone(request.phone());
        partner.setEmail(request.email());
        return supportPartnerRepository.save(partner).getId().toString();
    }

    public void deletePartner(UUID partnerId) {
        final SupportPartner partner = supportPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("No support partner found with id: " + partnerId));
        supportPartnerRepository.delete(partner);
    }
}
