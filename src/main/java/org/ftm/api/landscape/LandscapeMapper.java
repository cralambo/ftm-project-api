package org.ftm.api.landscape;

public class LandscapeMapper {
    public static Landscape mapToCreateLandscapeRequest(LandscapeRequest request) {
        return Landscape.builder()
                .name(request.name())
                .build();
    }

    public static LandscapeResponse mapToLandscapeResponse(Landscape landscape) {
        return LandscapeResponse.builder()
                .id(landscape.getId().toString())
                .name(landscape.getName())
                .build();
    }

    public static ShortLandscapeResponse mapToShortLandscapeResponse(Landscape landscape) {
        return ShortLandscapeResponse.builder()
                .id(landscape.getId().toString())
                .name(landscape.getName())
                .build();
    }
}
