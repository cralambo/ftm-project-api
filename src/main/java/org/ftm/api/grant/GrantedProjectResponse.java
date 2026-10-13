package org.ftm.api.grant;

import lombok.Builder;

@Builder
public record GrantedProjectResponse(
        String id,
        String grant_date,
        String grant_amount
) {
}
