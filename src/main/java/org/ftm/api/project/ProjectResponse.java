package org.ftm.api.project;

import lombok.Builder;
import org.ftm.api.grant.GrantedProjectResponse;
import org.ftm.api.partner.ShortSupportPartnerResponse;

@Builder
public record ProjectResponse(
        String id,
        String agreement_number,
        String title,
        String start_date,
        String end_date,
        String status,
        ShortSupportPartnerResponse support_partner,
        GrantedProjectResponse granted_project
) {
}
