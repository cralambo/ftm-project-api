package org.ftm.api.project;

import lombok.Builder;

@Builder
public record ProjectCoFunderRequest(
        String co_funder_id,
        String amount
) {
}
