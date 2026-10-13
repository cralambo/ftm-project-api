package org.ftm.api.project_co_funder;

import lombok.Builder;

@Builder
public record ShortProjectCoFunderResponse(
    String id,
    String co_funder_name,
    String amount
) {
}
