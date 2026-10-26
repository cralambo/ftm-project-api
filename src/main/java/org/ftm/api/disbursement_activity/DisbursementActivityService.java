package org.ftm.api.disbursement_activity;

import lombok.RequiredArgsConstructor;
import org.ftm.api.disbursement_schedule.DisbursementSchedule;
import org.ftm.api.disbursement_schedule.DisbursementScheduleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisbursementActivityService {

    private final DisbursementActivityRepository disbursementActivityRepository;
    private final DisbursementScheduleRepository disbursementScheduleRepository;

    public List<DisbursementActivityResponse> getActivitiesByScheduleId(UUID disbursementScheduleId) {
        if (!disbursementScheduleRepository.existsById(disbursementScheduleId)) {
            throw notFound("Calendrier de décaissement", disbursementScheduleId);
        }
        return disbursementActivityRepository.findByDisbursementScheduleIdOrderByIdAsc(disbursementScheduleId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public DisbursementActivityResponse createActivity(
            UUID scheduleId,
            DisbursementActivityRequest request
    ) {
        DisbursementSchedule schedule = disbursementScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> notFound("Calendrier de décaissement", scheduleId));
        if (disbursementActivityRepository.existsByDisbursementScheduleIdAndNameIgnoreCase(scheduleId, request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Une activité nommée '" + request.name() + "' existe déjà dans ce calendrier");
        }
        DisbursementActivity activity;
        if (request instanceof QuantitativeActivityRequest q) {
            QuantitativeActivity quantitativeActivity = new QuantitativeActivity();
            quantitativeActivity.setTargetQuantity(new BigDecimal(q.target_quantity()));
            quantitativeActivity.setUnit(q.unit());
            activity = quantitativeActivity;
        } else if (request instanceof QualitativeActivityRequest q) {
            QualitativeActivity qualitativeActivity = new QualitativeActivity();
            qualitativeActivity.setSuccessCriteria(q.success_criteria());
            activity = qualitativeActivity;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type d'activité inconnu");
        }
        activity.setName(request.name());
        activity.setDescription(request.description());
        activity.setDisbursementSchedule(schedule);
        return toResponse(disbursementActivityRepository.save(activity));
    }

    public DisbursementActivityResponse getActivityById(UUID activityId) {
        return toResponse(load(activityId));
    }

    @Transactional
    public DisbursementActivityResponse updateActivity(
            UUID activityId,
            DisbursementActivityRequest request
    ) {
        DisbursementActivity activity = load(activityId);

        if (request instanceof QuantitativeActivityRequest q) {
            if (!(activity instanceof QuantitativeActivity a)) {
                throw typeChangeForbidden();
            }
            a.setTargetQuantity(new BigDecimal(q.target_quantity()));
            a.setUnit(q.unit());
        } else if (request instanceof QualitativeActivityRequest q) {
            if (!(activity instanceof QualitativeActivity a)) {
                throw typeChangeForbidden();
            }
            a.setSuccessCriteria(q.success_criteria());
        }

        activity.setName(request.name());
        activity.setDescription(request.description());
        return toResponse(activity);
    }

    @Transactional
    public void deleteActivity(UUID activityId) {
        DisbursementActivity activity = load(activityId);
        try {
            disbursementActivityRepository.delete(activity);
            disbursementActivityRepository.flush();   // force la vérification des clés étrangères ici
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Impossible de supprimer : l'activité est utilisée dans un rapport");
        }
    }

    //==================== INTERNE =============================

    private DisbursementActivity load(UUID id) {
        return disbursementActivityRepository.findById(id)
                .orElseThrow(() -> notFound("Activity", id));
    }

    private DisbursementActivityResponse toResponse(DisbursementActivity a) {
        UUID scheduleId = a.getDisbursementSchedule().getId();
        if (a instanceof QuantitativeActivity q) {
            return DisbursementActivityResponse.builder()
                    .id(q.getId().toString())
                    .name(q.getName())
                    .schedule_id(scheduleId.toString())
                    .type("QUANTITATIVE")
                    .description(q.getDescription())
                    .target_quantity(
                            q.getTargetQuantity() != null ? q.getTargetQuantity().toPlainString() : null
                    )
                    .unit(q.getUnit())
                    .build();
        }
        QualitativeActivity q = (QualitativeActivity) a;
        return DisbursementActivityResponse.builder()
                .id(q.getId().toString())
                .schedule_id(scheduleId.toString())
                .type("QUALITATIVE")
                .name(q.getName())
                .success_criteria(q.getSuccessCriteria())
                .build();
    }

    private ResponseStatusException notFound(String what, UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, what + " introuvable : " + id);
    }

    private ResponseStatusException typeChangeForbidden() {
        return new ResponseStatusException(HttpStatus.CONFLICT,
                "Le type d'une activité ne peut pas être modifié : supprime-la et recrée-la");
    }
}
