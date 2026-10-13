package org.ftm.api.sub_landscape;

import org.ftm.api.landscape.LandscapeMapper;

public class SubLandscapeMapper {
    public static SubLandscape mapToCreateSubLandscapeRequest(SubLandscapeRequest request) {
        return SubLandscape.builder()
                .name(request.name())
                .build();
    }

    public static SubLandscapeResponse mapToSubLandscapeResponse(SubLandscape subLandscape) {
        return SubLandscapeResponse.builder()
                .id(subLandscape.getId().toString())
                .name(subLandscape.getName())
                .landscape(LandscapeMapper.mapToShortLandscapeResponse(subLandscape.getLandscape()))
                .build();
    }

    public static ShortSubLandscapeResponse mapToShortSubLandscapeResponse(SubLandscape subLandscape) {
        return ShortSubLandscapeResponse.builder()
                .id(subLandscape.getId().toString())
                .name(subLandscape.getName())
                .build();
    }
}
