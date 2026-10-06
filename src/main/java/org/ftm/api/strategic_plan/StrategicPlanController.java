package org.ftm.api.strategic_plan;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/strategic-plans")
public class StrategicPlanController {

    private final StrategicPlanService strategicPlanService;

    @PostMapping("/create")
    public ResponseEntity<UUID> createStrategicPlan(@RequestBody StrategicPlanRequest request) {
        return ResponseEntity.ok(strategicPlanService.createStrategicPlan(request));
    }

    @GetMapping("/{strategic-plan-id}")
    public ResponseEntity<StrategicPlanResponse> getStrategicPlanById(@PathVariable("strategic-plan-id") UUID strategicPlanId) {
        return ResponseEntity.ok(strategicPlanService.getStrategicPlanById(strategicPlanId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<StrategicPlanResponse>> getStrategicPlans(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(strategicPlanService.getStrategicPlans(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShortStrategicPlanResponse>> getAllStrategicPlans() {
        return ResponseEntity.ok(strategicPlanService.getAllStrategicPlans());
    }

    @PutMapping("/update/{strategic-plan-id}")
    public ResponseEntity<UUID> updateStrategicPlan(
            @PathVariable("strategic-plan-id") UUID strategicPlanId,
            @RequestBody StrategicPlanRequest request
    ) {
        return ResponseEntity.ok(strategicPlanService.updateStrategicPlan(strategicPlanId, request));
    }

    @DeleteMapping("/{strategic-plan-id}")
    public ResponseEntity<Void> deleteStrategicPlan(@PathVariable("strategic-plan-id") UUID strategicPlanId) {
        strategicPlanService.deleteStrategicPlan(strategicPlanId);
        return ResponseEntity.noContent().build();
    }
}
