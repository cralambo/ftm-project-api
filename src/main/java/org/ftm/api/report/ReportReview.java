package org.ftm.api.report;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "report_reviews",
        indexes = {
                @Index(name = "idx_report_reviews_report_date",
                        columnList = "partner_report_id,reviewed_at"),
                @Index(name = "idx_report_reviews_reviewer",
                        columnList = "reviewer_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReportReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "partner_report_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_report_review_report")
    )
    private PartnerReport partnerReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "reviewer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_report_review_user")
    )
    private User reviewer;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false, length = 30)
    private ReportReviewType reviewType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReportReviewDecision decision;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comments;

    @CreatedDate
    @Column(name = "reviewed_at", nullable = false, updatable = false)
    private LocalDateTime reviewedAt;
}
