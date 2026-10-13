package org.ftm.api.disbursement_schedule;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.project.Project;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "disbursement_schedules")
@EntityListeners(AuditingEntityListener.class)
public class DisbursementSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reference", nullable = false, length = 10)
    private String reference;

    @Column(name = "disbursement_month", nullable = false)
    private LocalDate disbursementMonth;

    @Column(name = "disbursement_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal disbursementAmount;

    @Column(name = "percentage", nullable = false, precision = 19, scale = 2)
    private BigDecimal percentage;

    @Column(name = "report_submission_date", nullable = false)
    private LocalDate reportSubmissionDate;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;
}
