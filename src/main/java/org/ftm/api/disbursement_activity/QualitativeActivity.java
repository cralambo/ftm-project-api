package org.ftm.api.disbursement_activity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("QUALITATIVE")
@Getter @Setter
public class QualitativeActivity extends DisbursementActivity {
    @Column(name = "success_criteria", columnDefinition = "TEXT", nullable = false)
    private String successCriteria;
}
