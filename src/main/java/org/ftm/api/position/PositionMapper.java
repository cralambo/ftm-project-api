package org.ftm.api.position;

public class PositionMapper {
    public static Position mapToCreateRequest(PositionRequest request) {
        return Position.builder()
                .code(request.code())
                .name(request.name())
                .build();
    }

    public static PositionResponse mapToPositionResponse(Position position) {
        return PositionResponse.builder()
                .id(position.getId().toString())
                .code(position.getCode())
                .name(position.getName())
                .build();
    }

    public static ShortPositionResponse mapToShortPositionResponse(Position position) {
        return ShortPositionResponse.builder()
                .id(position.getId().toString())
                .code(position.getCode())
                .name(position.getName())
                .build();
    }
}
