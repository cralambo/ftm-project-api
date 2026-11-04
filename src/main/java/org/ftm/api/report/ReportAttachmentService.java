
package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.user.User;
import org.ftm.api.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportAttachmentService {

    private static final long MAX_FILE_SIZE = 50L * 1024 * 1024;

    private static final Set<ReportAttachmentType> REQUIRED_GENERAL_ATTACHMENT_TYPES =
            Collections.unmodifiableSet(EnumSet.of(
                    ReportAttachmentType.TECHNICAL_SUPPORT,
                    ReportAttachmentType.FINANCIAL_SUPPORT,
                    ReportAttachmentType.OFFICIAL_LETTER,
                    ReportAttachmentType.CONTRACT,
                    ReportAttachmentType.REQUEST_FOR_NEXT_DISBURSEMENT
            ));

    private final ReportAttachmentRepository reportAttachmentRepository;
    private final PartnerReportRepository partnerReportRepository;
    private final TechnicalReportItemRepository technicalReportItemRepository;
    private final FinancialReportItemRepository financialReportItemRepository;
    private final UserRepository userRepository;
    private final ReportAttachmentStorageService reportAttachmentStorageService;

    /**
     * Upload d'une pièce jointe globale, technique ou financière.
     */
    @Transactional
    public ReportAttachmentDto upload(
            UUID reportId,
            UUID technicalItemId,
            UUID financialItemId,
            UUID currentUserId,
            MultipartFile file,
            String documentType,
            String name,
            String description
    ) throws IOException {

        validateFile(file);
        validateName(name);

        if (technicalItemId != null && financialItemId != null) {
            throw new IllegalArgumentException(
                    "An attachment cannot target both item types"
            );
        }

        ReportAttachmentType attachmentType = parseDocumentType(documentType);

        PartnerReport report = partnerReportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Report not found"
                        )
                );

        ensureReportEditable(report);

        TechnicalReportItem technicalReportItem = null;
        FinancialReportItem financialReportItem = null;

        if (technicalItemId != null) {
            technicalReportItem = technicalReportItemRepository
                    .findByIdAndPartnerReportId(technicalItemId, reportId)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Technical item does not belong to this report"
                            )
                    );
        }

        if (financialItemId != null) {
            financialReportItem = financialReportItemRepository
                    .findByIdAndPartnerReportId(financialItemId, reportId)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Financial item does not belong to this report"
                            )
                    );
        }

        User uploadedBy = userRepository.findById(currentUserId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Uploader not found"
                        )
                );

        String storageKey = reportAttachmentStorageService.store(file);

        try {
            ReportAttachment attachment = ReportAttachment.builder()
                    .partnerReport(report)
                    .technicalReportItem(technicalReportItem)
                    .financialReportItem(financialReportItem)
                    .originalFilename(
                            safeFilename(Objects.requireNonNull(file.getOriginalFilename()))
                    )
                    .storageKey(storageKey)
                    .contentType(detectContentType(file))
                    .fileSize(file.getSize())
                    .documentType(attachmentType)
                    .name(name.trim())
                    .description(description)
                    .uploadedBy(uploadedBy)
                    .build();

            return toDto(
                    reportAttachmentRepository.saveAndFlush(attachment)
            );

        } catch (RuntimeException exception) {
            reportAttachmentStorageService.delete(storageKey);
            throw exception;
        }
    }

    /**
     * Vérifie les cinq justificatifs globaux obligatoires.
     * Les pièces jointes techniques et financières ne sont pas prises en compte.
     * OTHER et documentType NULL restent facultatifs.
     */
    @Transactional(readOnly = true)
    public void validateRequiredGeneralAttachments(UUID reportId) {

        if (!partnerReportRepository.existsById(reportId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Report not found"
            );
        }

        List<ReportAttachment> attachments =
                reportAttachmentRepository
                        .findByPartnerReportIdOrderByUploadedAtDesc(reportId);

        Set<ReportAttachmentType> uploadedTypes = attachments.stream()
                .filter(attachment ->
                        attachment.getTechnicalReportItem() == null
                                && attachment.getFinancialReportItem() == null
                )
                .map(ReportAttachment::getDocumentType)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<ReportAttachmentType> missingTypes =
                EnumSet.copyOf(REQUIRED_GENERAL_ATTACHMENT_TYPES);

        missingTypes.removeAll(uploadedTypes);

        if (!missingTypes.isEmpty()) {

            String missingDocuments = missingTypes.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Impossible de soumettre le rapport. "
                            + "Documents obligatoires manquants : "
                            + missingDocuments
            );
        }
    }

    /**
     * Vérifie que le rapport est encore modifiable.
     */
    private void ensureReportEditable(PartnerReport report) {

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attachments can only be changed while the report is in DRAFT"
            );
        }
    }

    /**
     * Conversion sécurisée du String vers l'Enum.
     * Une valeur absente est autorisée.
     */
    private ReportAttachmentType parseDocumentType(String documentType) {

        if (documentType == null || documentType.isBlank()) {
            return null;
        }

        try {
            return ReportAttachmentType.valueOf(
                    documentType.trim().toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid document type: " + documentType
            );
        }
    }

    /**
     * Validation du nom du document.
     */
    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The attachment name is required"
            );
        }

        if (name.trim().length() > 150) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The attachment name must not exceed 150 characters"
            );
        }
    }

    /**
     * Validation du fichier.
     */
    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A file is required"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.CONTENT_TOO_LARGE,
                    "Maximum file size is 50 MB"
            );
        }

        String filename = file.getOriginalFilename();

        if (filename == null || filename.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid filename"
            );
        }
    }

    /**
     * Sécurisation du nom original du fichier.
     */
    private String safeFilename(String filename) {

        String normalized = filename.replace('\\', '/');

        normalized = normalized.substring(
                normalized.lastIndexOf('/') + 1
        );

        if (normalized.length() > 255) {
            normalized = normalized.substring(
                    normalized.length() - 255
            );
        }

        return normalized;
    }

    /**
     * Détection du Content-Type.
     */
    private String detectContentType(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return contentType;
    }

    /**
     * Conversion Entity vers DTO.
     */
    private ReportAttachmentDto toDto(ReportAttachment attachment) {

        return ReportAttachmentDto.builder()
                .id(attachment.getId().toString())
                .original_filename(attachment.getOriginalFilename())
                .file_size(String.valueOf(attachment.getFileSize()))
                .content_type(attachment.getContentType())
                .document_type(
                        attachment.getDocumentType() == null
                                ? null
                                : attachment.getDocumentType().name()
                )
                .name(attachment.getName())
                .description(attachment.getDescription())
                .uploaded_at(
                        attachment.getUploadedAt() == null
                                ? null
                                : attachment.getUploadedAt().toString()
                )
                .build();
    }

    /**
     * Liste des pièces jointes selon leur niveau d'association.
     */
    @Transactional(readOnly = true)
    public List<ReportAttachmentDto> list(
            UUID reportId,
            UUID technicalItemId,
            UUID financialItemId
    ) {

        if (!partnerReportRepository.existsById(reportId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Report not found"
            );
        }

        if (technicalItemId != null && financialItemId != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only one attachment target can be specified"
            );
        }

        if (technicalItemId != null) {

            return reportAttachmentRepository
                    .findByPartnerReportIdAndTechnicalReportItemIdOrderByUploadedAtDesc(
                            reportId,
                            technicalItemId
                    )
                    .stream()
                    .map(this::toDto)
                    .toList();
        }

        if (financialItemId != null) {

            return reportAttachmentRepository
                    .findByPartnerReportIdAndFinancialReportItemIdOrderByUploadedAtDesc(
                            reportId,
                            financialItemId
                    )
                    .stream()
                    .map(this::toDto)
                    .toList();
        }

        return reportAttachmentRepository
                .findByPartnerReportIdOrderByUploadedAtDesc(reportId)
                .stream()
                .filter(attachment ->
                        attachment.getTechnicalReportItem() == null
                                && attachment.getFinancialReportItem() == null
                )
                .map(this::toDto)
                .toList();
    }
}
