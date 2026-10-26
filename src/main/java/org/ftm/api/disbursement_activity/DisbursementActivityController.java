package org.ftm.api.disbursement_activity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disbursement-activities")
public class DisbursementActivityController {

    private final DisbursementActivityService disbursementActivityService;

    /** Acitivités d'un calendrier de décaissement */
    @GetMapping("/{schedule-id}/activities")
    public ResponseEntity<List<DisbursementActivityResponse>> getActivitiesByScheduleId(
            @PathVariable("schedule-id") UUID disbursementScheduleId
    ) {
        return ResponseEntity.ok(disbursementActivityService.getActivitiesByScheduleId(disbursementScheduleId));
    }

    /** Ajoute une activité (quantitative ou qualitative) à un calendrier */
    @PostMapping("/{schedule-id}/activities")
    public ResponseEntity<DisbursementActivityResponse> createActivity(
            @PathVariable("schedule-id") UUID scheduleId,
            @Valid @RequestBody DisbursementActivityRequest request
    ) {
        return ResponseEntity.ok(disbursementActivityService.createActivity(scheduleId, request));
    }

    @GetMapping("/{activity-id}")
    public ResponseEntity<DisbursementActivityResponse> getActivityById(@PathVariable("activity-id") UUID activityId) {
        return ResponseEntity.ok(disbursementActivityService.getActivityById(activityId));
    }

    @PutMapping("/{activity-id}")
    public ResponseEntity<DisbursementActivityResponse> updateActivity(
            @PathVariable("activity-id") UUID activityId,
            @RequestBody @Valid DisbursementActivityRequest request
    ) {
        return ResponseEntity.ok(disbursementActivityService.updateActivity(activityId, request));
    }

    @DeleteMapping("/{activity-id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable("activity-id") UUID activityId) {
        disbursementActivityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
