package org.ftm.api.report;

import lombok.Builder;

@Builder
public record UpdatePartnerReportRequest(
        String general_comments
) {
}
