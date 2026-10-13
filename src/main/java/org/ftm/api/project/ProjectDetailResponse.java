package org.ftm.api.project;

import lombok.Builder;
import org.ftm.api.disbursement_schedule.DisbursementScheduleResponse;
import org.ftm.api.district.ShortDistrictResponse;
import org.ftm.api.grant.GrantedProjectResponse;
import org.ftm.api.landscape.ShortLandscapeResponse;
import org.ftm.api.municipality.ShortMunicipalityResponse;
import org.ftm.api.organization.ShortLocalOrganizationResponse;
import org.ftm.api.partner.ShortSupportPartnerResponse;
import org.ftm.api.project_co_funder.ShortProjectCoFunderResponse;
import org.ftm.api.region.RegionResponse;
import org.ftm.api.strategic_plan.StrategicPlanResponse;
import org.ftm.api.sub_landscape.ShortSubLandscapeResponse;
import org.ftm.api.topic.ShortTopicResponse;

import java.util.Set;

@Builder
public record ProjectDetailResponse(
        String id,
        String agreement_number,
        String title,
        String start_date,
        String end_date,
        String own_funds,
        String organization_contribution,
        String co_funders_contribution,
        String description,
        String status,
        RegionResponse region,
        ShortDistrictResponse district,
        ShortMunicipalityResponse municipality,
        ShortLandscapeResponse landscape,
        ShortSubLandscapeResponse sub_landscape,
        ShortSupportPartnerResponse support_partner,
        ShortLocalOrganizationResponse local_organization,
        StrategicPlanResponse strategic_plan,
        GrantedProjectResponse granted_project,
        Set<ShortTopicResponse> topics,
        Set<ShortProjectCoFunderResponse> project_co_funders,
        Set<DisbursementScheduleResponse> disbursement_schedules
) {
}
