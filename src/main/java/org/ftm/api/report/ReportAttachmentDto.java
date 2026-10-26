package org.ftm.api.report;

import lombok.Builder;

@Builder
public record ReportAttachmentDto(
        String id,
        String original_filename,
        String file_size,
        String document_type,
        String description,
        String uploaded_at
) {
}
