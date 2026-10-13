package org.ftm.api.municipality;

import org.ftm.api.district.DistrictMapper;

public class MunicipalityMapper {

    public static Municipality mapToCreateMunicipalityRequest(MunicipalityRequest request) {
        return Municipality.builder().name(request.name()).build();
    }

    public static MunicipalityResponse mapToMunicipalityResponse(Municipality municipality) {
        return MunicipalityResponse.builder()
                .id(municipality.getId().toString())
                .name(municipality.getName())
                .district(DistrictMapper.mapToShortDistrictResponse(municipality.getDistrict()))
                .build();
    }

    public static ShortMunicipalityResponse mapToShortMunicipalityResponse(Municipality municipality) {
        return ShortMunicipalityResponse.builder()
                .id(municipality.getId().toString())
                .name(municipality.getName())
                .build();
    }
}
