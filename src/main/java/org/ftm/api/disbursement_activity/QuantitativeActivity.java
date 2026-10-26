package org.ftm.api.disbursement_activity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("QUANTITATIVE")
@Getter @Setter
public class QuantitativeActivity extends DisbursementActivity {

    @Column(name = "target_quantity", precision = 19, scale = 2, nullable = false)
    private BigDecimal targetQuantity;

    @Column(nullable = false, length = 50)
    private String unit;
}
