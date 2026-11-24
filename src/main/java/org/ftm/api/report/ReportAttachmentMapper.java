package org.ftm.api.report;

public class ReportAttachmentMapper {

    public static ReportAttachmentResponse toAttachmentResponse(ReportAttachment attachment) {
        return ReportAttachmentResponse.builder()
                .id(attachment.getId().toString())
                .original_filename(attachment.getOriginalFilename())
                .name(attachment.getName())
                .content_type(attachment.getContentType())
                .description(attachment.getDescription())
                .file_size(attachment.getFileSize())
                .document_type( attachment.getDocumentType() != null ? attachment.getDocumentType().name() : null)
                .uploaded_by(attachment.getUploadedBy().getFirstname() + " " + attachment.getUploadedBy().getLastname())
                .uploaded_at(attachment.getUploadedAt() == null ? null : attachment.getUploadedAt().toString())
                .build();
    }
}
