package org.ftm.api.project;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;
import org.ftm.api.district.District;
import org.ftm.api.grant.GrantedProject;
import org.ftm.api.landscape.Landscape;
import org.ftm.api.municipality.Municipality;
import org.ftm.api.organization.LocalOrganization;
import org.ftm.api.partner.SupportPartner;
import org.ftm.api.project_co_funder.ProjectCoFunder;
import org.ftm.api.region.Region;
import org.ftm.api.strategic_plan.StrategicPlan;
import org.ftm.api.sub_landscape.SubLandscape;
import org.ftm.api.topic.Topic;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "agreement_number", nullable = false, unique = true, length = 50)
    private String agreementNumber;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "period", precision = 19, scale = 2)
    private BigDecimal period;

    @Column(name = "own_funds", precision = 19, scale = 2)
    private BigDecimal ownFunds;

    @Column(name = "organization_contribution", precision = 19, scale = 2)
    private BigDecimal organizationContribution;

    @Column(name = "co_funders_contribution", precision = 19, scale = 2)
    private BigDecimal coFundersContribution;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "landscape_id")
    private Landscape landscape;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sub_landscape_id")
    private SubLandscape subLandscape;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "support_partner_id")
    private SupportPartner supportPartner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "local_organization_id")
    private LocalOrganization localOrganization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "strategic_plan_id")
    private StrategicPlan strategicPlan;

    @OneToOne(mappedBy = "project")
    private GrantedProject grantedProject;

    @ManyToMany
    @JoinTable(
            name = "project_topics",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    @Builder.Default
    private Set<Topic> topics = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProjectCoFunder> projectCoFunders = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private Set<DisbursementSchedule> disbursementSchedules = new HashSet<>();

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;

}
