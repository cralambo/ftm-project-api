package org.ftm.api.project;

import lombok.Builder;
import org.ftm.api.grant.GrantedProjectRequest;

import java.util.List;
import java.util.Set;

@Builder
public record ProjectRequest(
        String agreement_number,
        String title,
        String start_date,
        String end_date,
        String own_funds,
        String organization_contribution,
        String description,
        String status,
        String region_id,
        String district_id,
        String municipality_id,
        String landscape_id,
        String sub_landscape_id,
        String support_partner_id,
        String local_organization_id,
        String strategic_plan_id,
        Set<String> topic_ids,
        Set<ProjectCoFunderRequest> co_funders,
        GrantedProjectRequest granted_project
) {
}
