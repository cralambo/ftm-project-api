package org.ftm.api.grant;

import lombok.Builder;

@Builder
public record GrantedProjectRequest(
        String grant_date,
        String grant_amount
) {
}
