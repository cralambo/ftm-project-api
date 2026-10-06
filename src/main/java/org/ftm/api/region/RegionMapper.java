package org.ftm.api.region;

public class RegionMapper {

    public static Region mapToCreateRegionRequest(RegionRequest request) {
        return Region.builder()
                .name(request.name())
                .build();
    }

    public static RegionResponse mapToRegionResponse(Region region) {
        return RegionResponse.builder()
                .region_id(region.getId().toString())
                .name(region.getName())
                .build();
    }
}
