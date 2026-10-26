package org.ftm.api.report;

import lombok.RequiredArgsConstructor;
import org.ftm.api.disbursement_activity.DisbursementActivity;
import org.ftm.api.disbursement_activity.DisbursementActivityRepository;
import org.ftm.api.security.CustomUserDetails;
import org.ftm.api.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TechnicalReportItemService {

    private final TechnicalReportItemRepository technicalReportItemRepository;
    private final ReportCommonService reportCommonService;
    private final DisbursementActivityRepository disbursementActivityRepository;

    @Transactional(readOnly = true)
    public List<TechnicalReportItemResponse> list(UUID reportId, CustomUserDetails userDetails
    ) {
        PartnerReport report = reportCommonService.getReport(reportId);
        User actor = reportCommonService.getCurrentUser(userDetails);
        reportCommonService.ensureCanRead(report, actor);
        return technicalReportItemRepository.findByPartnerReportId(reportId)
                .stream()
                .map(TechnicalReportItemMapper::mapToTechnicalReportItemResponse)
                .toList();

    }

    @Transactional
    public TechnicalReportItemResponse upsert(
            UUID reportId,
            UUID activityId,
            UpsertTechnicalReportItemRequest request,
            CustomUserDetails userDetails
    ) {
        User actor = reportCommonService.getCurrentUser(userDetails);
        PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        DisbursementActivity activity = disbursementActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Activité de décaissement introuvable."
                ));
        if (activity.getDisbursementSchedule() == null
                || !activity.getDisbursementSchedule().getId()
                .equals(report.getDisbursementSchedule().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indiquez une quantité atteinte ou une description du résultat."
            );
        }
        TechnicalReportItem item = technicalReportItemRepository
                .findByPartnerReportIdAndDisbursementActivityId(reportId, activityId)
                .orElseGet(TechnicalReportItem::new);
        item.setPartnerReport(report);
        item.setDisbursementActivity(activity);
        item.setAchievedQuantity(
                request.achieved_quantity() == null ? null : new BigDecimal(request.achieved_quantity())
        );
        item.setAchievedUnit(request.achieved_unit());
        item.setResultDescription(request.result_description());
        item.setObservations(request.observations());

        TechnicalReportItem savedItem = technicalReportItemRepository.save(item);

        return TechnicalReportItemMapper.mapToTechnicalReportItemResponse(savedItem);
    }

    public void deleteItem(UUID reportId, UUID activityId, CustomUserDetails userDetails) {
        PartnerReport report = reportCommonService.getReportForUpdate(reportId);
        User actor = reportCommonService.getCurrentUser(userDetails);
        reportCommonService.ensureOwner(report, actor);
        reportCommonService.ensureEditable(report);

        technicalReportItemRepository.findByPartnerReportIdAndDisbursementActivityId(reportId, activityId)
                .ifPresent(technicalReportItemRepository::delete);
    }
}
