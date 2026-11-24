package org.ftm.api.report;

import lombok.Builder;

@Builder
public record ReportAttachmentResponse(
        String id,
        String original_filename,
        String name,
        String content_type,
        Long file_size,
        String document_type,
        String description,
        String uploaded_by,
        String uploaded_at
) {
}
