package org.ftm.api.project;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.co_funder.CoFunder;
import org.ftm.api.co_funder.CoFunderRepository;
import org.ftm.api.common.PageResponse;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;
import org.ftm.api.disbursement_schedule.DisbursementScheduleRepository;
import org.ftm.api.district.District;
import org.ftm.api.district.DistrictRepository;
import org.ftm.api.grant.GrantedProject;
import org.ftm.api.grant.GrantedProjectRepository;
import org.ftm.api.landscape.Landscape;
import org.ftm.api.landscape.LandscapeRepository;
import org.ftm.api.municipality.Municipality;
import org.ftm.api.municipality.MunicipalityRepository;
import org.ftm.api.organization.LocalOrganization;
import org.ftm.api.organization.LocalOrganizationRepository;
import org.ftm.api.partner.SupportPartner;
import org.ftm.api.partner.SupportPartnerRepository;
import org.ftm.api.project_co_funder.ProjectCoFunder;
import org.ftm.api.region.Region;
import org.ftm.api.region.RegionRepository;
import org.ftm.api.strategic_plan.StrategicPlan;
import org.ftm.api.strategic_plan.StrategicPlanRepository;
import org.ftm.api.sub_landscape.SubLandscape;
import org.ftm.api.sub_landscape.SubLandscapeRepository;
import org.ftm.api.topic.Topic;
import org.ftm.api.topic.TopicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final MunicipalityRepository municipalityRepository;
    private final LandscapeRepository landscapeRepository;
    private final SubLandscapeRepository subLandscapeRepository;
    private final SupportPartnerRepository supportPartnerRepository;
    private final LocalOrganizationRepository localOrganizationRepository;
    private final StrategicPlanRepository strategicPlanRepository;
    private final TopicRepository topicRepository;
    private final CoFunderRepository coFunderRepository;
    private final GrantedProjectRepository grantedProjectRepository;
    private final DisbursementScheduleRepository disbursementScheduleRepository;

    @Transactional
    public String createProject(ProjectRequest request) {

        final UUID regionId = UUID.fromString(request.region_id());
        final UUID districtId = UUID.fromString(request.district_id());
        final UUID municipalityId = UUID.fromString(request.municipality_id());
        final UUID landscapeId = UUID.fromString(request.landscape_id());
        final UUID subLandscapeId = UUID.fromString(request.sub_landscape_id());
        final UUID supportPartnerId = UUID.fromString(request.support_partner_id());
        final UUID localOrganizationId = UUID.fromString(request.local_organization_id());
        final UUID strategicPlanId = UUID.fromString(request.strategic_plan_id());

        // 1. Charger les relations
        final Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));

        final District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new EntityNotFoundException("District not found"));

        final Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new EntityNotFoundException("Municipality not found"));

        final Landscape landscape = landscapeRepository.findById(landscapeId)
                .orElseThrow(() -> new EntityNotFoundException("Landscape not found"));

        final SubLandscape subLandscape = subLandscapeRepository.findById(subLandscapeId)
                .orElseThrow(() -> new EntityNotFoundException("SubLandscape not found"));

        final SupportPartner supportPartner = supportPartnerRepository.findById(supportPartnerId)
                .orElseThrow(() -> new EntityNotFoundException("Support partner not found"));

        final LocalOrganization localOrganization =
                localOrganizationRepository.findById(localOrganizationId)
                        .orElseThrow(() -> new EntityNotFoundException("Local organization not found"));

        final StrategicPlan strategicPlan =
                strategicPlanRepository.findById(strategicPlanId)
                        .orElseThrow(() -> new EntityNotFoundException("Strategic plan not found"));

        // 2. Charger les topics
        Set<Topic> topics = new HashSet<>();

        if (request.topic_ids() != null && !request.topic_ids().isEmpty()) {

            Set<UUID> topicIds = request.topic_ids()
                    .stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            topics.addAll(topicRepository.findAllById(topicIds));

            // Vérification : tous les topics demandés existent-ils ?
            if (topics.size() != topicIds.size()) {

                Set<UUID> foundTopicIds = topics.stream()
                        .map(Topic::getId)
                        .collect(Collectors.toSet());

                Set<UUID> missingTopicIds = new HashSet<>(topicIds);
                missingTopicIds.removeAll(foundTopicIds);

                throw new IllegalArgumentException(
                        "Topics not found: " + missingTopicIds
                );
            }
        }

        // 3. Créer le Project
        Project project = Project.builder()
                .agreementNumber(request.agreement_number())
                .title(request.title())
                .startDate(request.start_date() != null ? LocalDate.parse(request.start_date()) : null)
                .endDate(request.end_date() != null ? LocalDate.parse(request.end_date()) : null)
                .ownFunds(new BigDecimal(request.own_funds()))
                .organizationContribution(new BigDecimal(request.organization_contribution()))
                .description(request.description())
                .status(ProjectStatus.valueOf(request.status()))
                .region(region)
                .district(district)
                .municipality(municipality)
                .landscape(landscape)
                .subLandscape(subLandscape)
                .supportPartner(supportPartner)
                .localOrganization(localOrganization)
                .strategicPlan(strategicPlan)
                .topics(topics)
                .build();

        // 4. Créer les ProjectCoFunders
        if (request.co_funders() != null) {

            for (ProjectCoFunderRequest coFunderRequest
                    : request.co_funders()) {

                UUID coFunderId =
                        UUID.fromString(coFunderRequest.co_funder_id());

                CoFunder coFunder =
                        coFunderRepository.findById(coFunderId)
                                .orElseThrow(() ->
                                        new EntityNotFoundException(
                                                "Co-funder not found: "
                                                        + coFunderRequest.co_funder_id()
                                        )
                                );

                ProjectCoFunder projectCoFunder =
                        ProjectCoFunder.builder()
                                .project(project)
                                .coFunder(coFunder)
                                .amount(new BigDecimal(coFunderRequest.amount()))
                                .build();

                project.getProjectCoFunders()
                        .add(projectCoFunder);
            }
        }

        project = projectRepository.save(project);



        // 5. Créer GrantedProject
        if (request.granted_project() != null) {

            GrantedProject grantedProject = GrantedProject.builder()
                            .project(project)
                            .grantDate(
                                    request.granted_project().grant_date() != null
                                            ? LocalDate.parse(request.granted_project().grant_date()) : null
                            )
                            .grantAmount(
                                    new BigDecimal(request.granted_project().grant_amount())
                            )
                            .build();

            grantedProjectRepository.save(grantedProject);
        }

        return project.getId().toString();
    }

    public PageResponse<ProjectResponse> getProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectRepository.findAllWithGrantedProjectAndSupportPartner(pageable);
        List<ProjectResponse> content = projects.stream()
                .map(ProjectMapper::mapToProjectResponse)
                .toList();
        return PageResponse.<ProjectResponse>builder()
                .content(content)
                .number(projects.getNumber())
                .size(projects.getSize())
                .totalElements(projects.getTotalElements())
                .totalPages(projects.getTotalPages())
                .first(projects.isFirst())
                .last(projects.isLast())
                .build();
    }

    public ProjectDetailResponse getProjectById(UUID projectId) {

        return projectRepository.findByIdWithAllRelations(projectId)
                .map(ProjectMapper::mapToProjectDetailResponse)
                .orElseThrow(() -> new EntityNotFoundException("No project found with id: " + projectId));
    }
}
