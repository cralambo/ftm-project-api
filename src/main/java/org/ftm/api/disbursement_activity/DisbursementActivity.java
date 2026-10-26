package org.ftm.api.disbursement_activity;

import jakarta.persistence.*;
import lombok.*;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;

import java.util.UUID;

@Entity
@Table(name = "disbursement_activities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@Getter @Setter
public abstract class DisbursementActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disbursement_schedule_id", nullable = false)
    private DisbursementSchedule disbursementSchedule;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
