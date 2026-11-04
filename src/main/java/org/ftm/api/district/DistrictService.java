package org.ftm.api.district;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.region.Region;
import org.ftm.api.region.RegionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final RegionRepository regionRepository;

    public String createDistrict(DistrictRequest request) {
        District district = DistrictMapper.mapToCreateDistrictRequest(request);
        final Region region = regionRepository.findById(UUID.fromString(request.region_id()))
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + request.region_id()));

        if (districtRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("");
        }

        district.setRegion(region);
        return districtRepository.save(district).getId().toString();
    }

    public PageResponse<DistrictResponse> getDistricts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("code").ascending());
        Page<District> districts = districtRepository.findAll(pageable);
        List<DistrictResponse> content = districts
                .stream()
                .map(DistrictMapper::mapToDistrictResponse)
                .toList();
        return PageResponse.<DistrictResponse>builder()
                .content(content)
                .number(districts.getNumber())
                .size(districts.getSize())
                .totalElements(districts.getTotalElements())
                .totalPages(districts.getTotalPages())
                .first(districts.isFirst())
                .last(districts.isLast())
                .build();
    }

    public List<ShortDistrictResponse> getAllDistricts() {
        List<District> districts = districtRepository.findAll(Sort.by("createdDate").descending());
        return districts
                .stream()
                .map(DistrictMapper::mapToShortDistrictResponse)
                .toList();
    }

    public List<ShortDistrictResponse> getDistrictsByRegionId(UUID regionId) {
        List<District> districts = districtRepository.findByRegionId(regionId);
        return districts.stream()
                .map(DistrictMapper::mapToShortDistrictResponse)
                .toList();
    }

    public DistrictResponse getDistrictById(UUID districtId) {
        return districtRepository.findById(districtId)
                .map(DistrictMapper::mapToDistrictResponse)
                .orElseThrow(() -> new EntityNotFoundException("No district found with id: " + districtId));
    }

    public String updateDistrict(UUID districtId, DistrictRequest request) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new EntityNotFoundException("No district found with id: " + districtId));
        final Region region = regionRepository.findById(UUID.fromString(request.region_id()))
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + request.region_id()));
        district.setCode(request.code());
        district.setName(request.name());
        district.setRegion(region);
        return districtRepository.save(district).getId().toString();
    }

    public void deleteDistrict(UUID districtId) {
        final District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new EntityNotFoundException("No district found with id: " + districtId));
        districtRepository.delete(district);
    }
}
