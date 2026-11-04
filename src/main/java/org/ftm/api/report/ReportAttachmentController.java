package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.security.CustomUserDetails;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/partner-reports")
@RequiredArgsConstructor
public class ReportAttachmentController {

    private final ReportAttachmentService reportAttachmentService;
    private final ReportAttachmentStorageService reportAttachmentStorageService;
    private final ReportAttachmentRepository reportAttachmentRepository;

    @PostMapping(
            value = "/{report-id}/attachments",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ReportAttachmentDto> uploadGeneral(
            @PathVariable("report-id") UUID reportId,
            @RequestPart("file")MultipartFile file,
            @RequestParam String name,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String description,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(upload(reportId, null, null, file, documentType, name, description, userId));
    }

    @PostMapping(
            value = "/{report-id}/technical-items/{item-id}/attachments",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ReportAttachmentDto> uploadTechnical(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId,
            @RequestPart("file") MultipartFile file,
            @RequestParam String name,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String description,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(upload(reportId, itemId, null, file, documentType, name, description, userId));
    }

    @PostMapping(
            value = "/{report-id}/financial-items/{item-id}/attachments",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ReportAttachmentDto> uploadFinancial(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId,
            @RequestPart("file") MultipartFile file,
            @RequestParam String name,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String description,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        UUID userId = userDetails.getId();
        return ResponseEntity.ok(upload(reportId, null, itemId, file, documentType, name, description, userId));
    }

    @GetMapping("/{report-id}/attachments")
    public ResponseEntity<List<ReportAttachmentDto>> listGeneral(@PathVariable("report-id") UUID reportId) {
        return ResponseEntity.ok(reportAttachmentService.list(reportId, null, null));
    }

    @GetMapping("/{report-id}/financial-items/{item-id}/attachments")
    public ResponseEntity<List<ReportAttachmentDto>> listFinancial(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId
    ) {
        return ResponseEntity.ok(reportAttachmentService.list(reportId, null, itemId));
    }

    @GetMapping("/{report-id}/technical-items/{item-id}/attachments")
    public ResponseEntity<List<ReportAttachmentDto>> listTechnical(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("item-id") UUID itemId
    ) {
        return ResponseEntity.ok(reportAttachmentService.list(reportId, itemId, null));
    }

    @GetMapping("/{report-id}/attachments/{attachment-id}/download")
    public ResponseEntity<StreamingResponseBody> download(
            @PathVariable("report-id") UUID reportId,
            @PathVariable("attachment-id") UUID attachmentId
    ) {
        ReportAttachment attachment = reportAttachmentRepository.findByIdAndPartnerReportId(attachmentId, reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attachment not found"
                ));

        final String storageKey = attachment.getStorageKey();
        final String originalFilename = attachment.getOriginalFilename();
        final String contentType = attachment.getContentType() != null
                ? attachment.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        final long fileSize = attachment.getFileSize();

        StreamingResponseBody responseBody = outputStream -> {
            try(var inputStream = reportAttachmentStorageService.load(storageKey)) {
                inputStream.transferTo(outputStream);
            } catch (IOException exception) {
                throw new UncheckedIOException(
                        "Unable to stream attachment",
                        exception
                );
            }
        };
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(originalFilename, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(parseContentType(contentType))
                .contentLength(fileSize)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .body(responseBody);
    }

    private MediaType parseContentType(String contentType) {
        try {
            return MediaType.parseMediaType(contentType);
        } catch (IllegalArgumentException exception) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private ReportAttachmentDto upload(
            UUID reportId,
            UUID technicalItemId,
            UUID financialItemId,
            MultipartFile file,
            String documentType,
            String name,
            String description,
            UUID currentUserId
    ) throws IOException {
        return reportAttachmentService.upload(
                reportId,
                technicalItemId,
                financialItemId,
                currentUserId,
                file,
                documentType,
                name,
                description
        );
    }
}
