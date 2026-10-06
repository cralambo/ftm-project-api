package org.ftm.api.region;

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
public class RegionService {

    private final RegionRepository regionRepository;

    public String createRegion(RegionRequest request) {
        final Region region = RegionMapper.mapToCreateRegionRequest(request);
        return regionRepository.save(region).getId().toString();
    }

    public PageResponse<RegionResponse> getRegions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Region> regions = regionRepository.findAll(pageable);
        List<RegionResponse> content = regions
                .stream()
                .map(RegionMapper::mapToRegionResponse)
                .toList();
        return PageResponse.<RegionResponse>builder()
                .content(content)
                .number(regions.getNumber())
                .size(regions.getSize())
                .totalElements(regions.getTotalElements())
                .totalPages(regions.getTotalPages())
                .first(regions.isFirst())
                .last(regions.isLast())
                .build();
    }

    public List<RegionResponse> getAllRegions() {
        List<Region> regions = regionRepository.findAll(Sort.by("createdDate").descending());
        return regions
                .stream()
                .map(RegionMapper::mapToRegionResponse)
                .toList();
    }

    public RegionResponse getRegionById(UUID regionId) {
        return regionRepository.findById(regionId)
                .map(RegionMapper::mapToRegionResponse)
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
    }

    public String updateRegion(UUID regionId, RegionRequest request) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
        region.setName(request.name());
        return regionRepository.save(region).getId().toString();
    }

    public void deleteRegion(UUID regionId) {
        final Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
        regionRepository.delete(region);
    }
}
