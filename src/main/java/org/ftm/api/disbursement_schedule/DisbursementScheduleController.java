package org.ftm.api.disbursement_schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/disbursement-schedules")
@RequiredArgsConstructor
public class DisbursementScheduleController {

    private final DisbursementScheduleService disbursementScheduleService;

    @PostMapping("/create")
    public ResponseEntity<String> createDisbursementSchedule(@RequestBody DisbursementScheduleRequest request) {
        return ResponseEntity.ok(disbursementScheduleService.create(request));
    }

    @GetMapping("/{schedule-id}")
    public ResponseEntity<DisbursementScheduleResponse> getDisbursementScheduleByID(@PathVariable("schedule-id") UUID scheduleId) {
        return ResponseEntity.ok(disbursementScheduleService.getScheduleById(scheduleId));
    }
}
