package org.ftm.api.district;

import org.ftm.api.region.RegionMapper;

public class DistrictMapper {

    public static District mapToCreateDistrictRequest(DistrictRequest request) {
        return District.builder()
                .code(request.code())
                .name(request.name())
                .build();
    }

    public static DistrictResponse mapToDistrictResponse(District district) {
        return DistrictResponse.builder()
                .district_id(district.getId().toString())
                .code(district.getCode())
                .name(district.getName())
                .region(RegionMapper.mapToRegionResponse(district.getRegion()))
                .build();
    }

    public static ShortDistrictResponse mapToShortDistrictResponse(District district) {
        return ShortDistrictResponse.builder()
                .district_id(district.getId().toString())
                .code(district.getCode())
                .name(district.getName())
                .build();
    }
}
