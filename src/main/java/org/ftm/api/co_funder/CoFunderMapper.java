package org.ftm.api.co_funder;

public class CoFunderMapper {
    public static CoFunder mapToCoFunderCreateRequest(CoFunderRequest request) {
        return CoFunder.builder()
                .name(request.name())
                .build();
    }

    public static CoFunderResponse mapToCoFunderResponse(CoFunder coFunder) {
        return CoFunderResponse.builder()
                .id(coFunder.getId().toString())
                .name(coFunder.getName())
                .build();
    }
}
