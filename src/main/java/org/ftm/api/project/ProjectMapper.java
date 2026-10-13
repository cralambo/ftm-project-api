package org.ftm.api.project;

import org.ftm.api.disbursement_schedule.DisbursementScheduleMapper;
import org.ftm.api.district.DistrictMapper;
import org.ftm.api.grant.GrantedProjectMapper;
import org.ftm.api.landscape.LandscapeMapper;
import org.ftm.api.municipality.MunicipalityMapper;
import org.ftm.api.organization.LocalOrganizationMapper;
import org.ftm.api.partner.SupportPartnerMapper;
import org.ftm.api.project_co_funder.ProjectCoFunderMapper;
import org.ftm.api.region.RegionMapper;
import org.ftm.api.strategic_plan.StrategicPlanMapper;
import org.ftm.api.sub_landscape.SubLandscapeMapper;
import org.ftm.api.topic.TopicMapper;

import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectResponse mapToProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId().toString())
                .agreement_number(project.getAgreementNumber())
                .title(project.getTitle())
                .start_date(project.getStartDate() != null ? project.getStartDate().toString() : null)
                .end_date(project.getEndDate() != null ? project.getEndDate().toString() : null)
                .status(project.getStatus().toString())
                .support_partner(SupportPartnerMapper.mapToShortSupportPartnerResponse(project.getSupportPartner()))
                .granted_project(GrantedProjectMapper.mapToGrantedProjectResponse(project.getGrantedProject()))
                .build();
    }

    public static ProjectDetailResponse mapToProjectDetailResponse(Project project) {
        return ProjectDetailResponse.builder()
                .id(project.getId().toString())
                .agreement_number(project.getAgreementNumber())
                .title(project.getTitle())
                .start_date(project.getStartDate() != null ? project.getStartDate().toString() : null)
                .end_date(project.getEndDate() != null ? project.getEndDate().toString() : null)
                .description(project.getDescription())
                .own_funds(
                        project.getOwnFunds() != null
                                ? project.getOwnFunds().toPlainString()
                                : null
                )
                .organization_contribution(
                        project.getOrganizationContribution() != null
                                ? project.getOrganizationContribution().toPlainString()
                                : null
                )
                .co_funders_contribution(
                        project.getCoFundersContribution() != null
                                ? project.getCoFundersContribution().toPlainString()
                                : null
                )
                .status(project.getStatus().toString())
                .region(RegionMapper.mapToRegionResponse(project.getRegion()))
                .district(DistrictMapper.mapToShortDistrictResponse(project.getDistrict()))
                .municipality(MunicipalityMapper.mapToShortMunicipalityResponse(project.getMunicipality()))
                .landscape(LandscapeMapper.mapToShortLandscapeResponse(project.getLandscape()))
                .sub_landscape(SubLandscapeMapper.mapToShortSubLandscapeResponse(project.getSubLandscape()))
                .support_partner(SupportPartnerMapper.mapToShortSupportPartnerResponse(project.getSupportPartner()))
                .local_organization(LocalOrganizationMapper.mapToShortLocalOrganization(project.getLocalOrganization()))
                .strategic_plan(StrategicPlanMapper.mapToStrategicPlanResponse(project.getStrategicPlan()))
                .granted_project(GrantedProjectMapper.mapToGrantedProjectResponse(project.getGrantedProject()))
                .topics(project.getTopics()
                        .stream()
                        .map(TopicMapper::mapToShortTopicResponse)
                        .collect(Collectors.toSet())
                )
                .project_co_funders(project.getProjectCoFunders()
                        .stream()
                        .map(ProjectCoFunderMapper::mapToShortProjectCoFunderResponse)
                        .collect(Collectors.toSet())
                )
                .disbursement_schedules(project.getDisbursementSchedules()
                        .stream()
                        .map(DisbursementScheduleMapper::mapToDisbursementScheduleResponse)
                        .collect(Collectors.toSet()))
                .build();
    }
}
