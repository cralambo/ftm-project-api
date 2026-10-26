package org.ftm.api.disbursement_activity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuantitativeActivityRequest.class, name = "QUANTITATIVE"),
        @JsonSubTypes.Type(value = QualitativeActivityRequest.class, name = "QUALITATIVE")
})
public sealed interface DisbursementActivityRequest permits QuantitativeActivityRequest, QualitativeActivityRequest {
    String name();
    String description();
}
