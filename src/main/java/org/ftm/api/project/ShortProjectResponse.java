package org.ftm.api.project;

import lombok.Builder;
import org.ftm.api.grant.GrantedProjectResponse;

@Builder
public record ShortProjectResponse(
        String id,
        String agreement_number,
        String title,
        String start_date,
        String end_date,
        String status,
        GrantedProjectResponse granted_project
) {
}
