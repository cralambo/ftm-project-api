package org.ftm.api.municipality;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.district.District;
import org.ftm.api.district.DistrictRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final DistrictRepository districtRepository;

    public String createMunicipality(MunicipalityRequest request) {
        Municipality municipality = MunicipalityMapper.mapToCreateMunicipalityRequest(request);
        final District district = districtRepository.findById(UUID.fromString(request.district_id()))
                .orElseThrow(() -> new EntityNotFoundException("No district found with id: " + request.district_id()));
        municipality.setDistrict(district);
        return municipalityRepository.save(municipality).getId().toString();
    }

    public PageResponse<MunicipalityResponse> getMunicipalities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Municipality> municipalities = municipalityRepository.findAll(pageable);
        List<MunicipalityResponse> content = municipalities
                .stream()
                .map(MunicipalityMapper::mapToMunicipalityResponse)
                .toList();
        return PageResponse.<MunicipalityResponse>builder()
                .content(content)
                .number(municipalities.getNumber())
                .size(municipalities.getSize())
                .totalElements(municipalities.getTotalElements())
                .totalPages(municipalities.getTotalPages())
                .first(municipalities.isFirst())
                .last(municipalities.isLast())
                .build();
    }

    public List<ShortMunicipalityResponse> getAllMunicipalities() {
        List<Municipality> municipalities = municipalityRepository.findAll(Sort.by("createdDate").descending());
        return municipalities
                .stream()
                .map(MunicipalityMapper::mapToShortMunicipalityResponse)
                .toList();
    }

    public MunicipalityResponse getMunicipalityById(UUID municipalityId) {
        return municipalityRepository.findById(municipalityId)
                .map(MunicipalityMapper::mapToMunicipalityResponse)
                .orElseThrow(() -> new EntityNotFoundException("No municipality found with id: " + municipalityId));
    }

    public List<ShortMunicipalityResponse> getMunicipalitiesByDistrictId(UUID districtId) {
        List<Municipality> municipalities = municipalityRepository.findByDistrictId(districtId);
        return municipalities
                .stream()
                .map(MunicipalityMapper::mapToShortMunicipalityResponse)
                .toList();
    }

    public String updateMunicipality(UUID municipalityId, MunicipalityRequest request) {
        Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new EntityNotFoundException("No municipality found with id: " + municipalityId));
        final District district = districtRepository.findById(UUID.fromString(request.district_id()))
                .orElseThrow(() -> new EntityNotFoundException("No district found with id: " + request.district_id()));
        municipality.setName(request.name());
        municipality.setDistrict(district);
        return municipalityRepository.save(municipality).getId().toString();
    }

    public void deleteMunicipality(UUID municipalityId) {
        Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new EntityNotFoundException("No municipality found with id: " + municipalityId));
        municipalityRepository.delete(municipality);
    }
}
