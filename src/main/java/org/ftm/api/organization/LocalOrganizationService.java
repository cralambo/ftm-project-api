package org.ftm.api.organization;

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
public class LocalOrganizationService {

    private final LocalOrganizationRepository localOrganizationRepository;

    public String createOrganization(LocalOrganizationRequest request) {
        final LocalOrganization organization = LocalOrganizationMapper.mapToCreateOrganizationRequest(request);
        return localOrganizationRepository.save(organization).getId().toString();
    }

    public LocalOrganizationResponse getOrganizationById(UUID organizationId) {
        return localOrganizationRepository.findById(organizationId)
                .map(LocalOrganizationMapper::mapToOrganizationResponse)
                .orElseThrow(() -> new EntityNotFoundException("No local organization found with id: " + organizationId));
    }

    public PageResponse<LocalOrganizationResponse> getOrganizations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<LocalOrganization> organizations = localOrganizationRepository.findAll(pageable);
        List<LocalOrganizationResponse> content = organizations.stream()
                .map(LocalOrganizationMapper::mapToOrganizationResponse)
                .toList();
        return PageResponse.<LocalOrganizationResponse>builder()
                .content(content)
                .number(organizations.getNumber())
                .size(organizations.getSize())
                .totalElements(organizations.getTotalElements())
                .totalPages(organizations.getTotalPages())
                .first(organizations.isFirst())
                .last(organizations.isLast())
                .build();
    }

    public String updateOrganization(UUID organizationId, LocalOrganizationRequest request) {
        LocalOrganization organization = localOrganizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("No local organization found with id: " + organizationId));
        organization.setName(request.name());
        organization.setAddress(request.address());
        organization.setPhone(request.phone());
        organization.setEmail(request.email());
        return localOrganizationRepository.save(organization).getId().toString();
    }

    public void deleteOrganization(UUID organizationId) {
        final LocalOrganization organization = localOrganizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("No local organization found with id: " + organizationId));
        localOrganizationRepository.delete(organization);
    }

    public List<ShortLocalOrganizationResponse> getAllLocalOrganizations() {
        List<LocalOrganization> localOrganizations = localOrganizationRepository.findAll(Sort.by("createdDate").descending());
        return localOrganizations
                .stream()
                .map(LocalOrganizationMapper::mapToShortLocalOrganization)
                .toList();
    }
}
