package org.ftm.api.strategic_plan;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/strategic-plans")
public class StrategicPlanController {

    private final StrategicPlanService strategicPlanService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('strategic_plans.create')")
    public ResponseEntity<UUID> createStrategicPlan(@RequestBody StrategicPlanRequest request) {
        return ResponseEntity.ok(strategicPlanService.createStrategicPlan(request));
    }

    @GetMapping("/{strategic-plan-id}")
    @PreAuthorize("hasAuthority('strategic_plans.view')")
    public ResponseEntity<StrategicPlanResponse> getStrategicPlanById(@PathVariable("strategic-plan-id") UUID strategicPlanId) {
        return ResponseEntity.ok(strategicPlanService.getStrategicPlanById(strategicPlanId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('strategic_plans.view')")
    public ResponseEntity<PageResponse<StrategicPlanResponse>> getStrategicPlans(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(strategicPlanService.getStrategicPlans(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('strategic_plans.view')")
    public ResponseEntity<List<ShortStrategicPlanResponse>> getAllStrategicPlans() {
        return ResponseEntity.ok(strategicPlanService.getAllStrategicPlans());
    }

    @PutMapping("/update/{strategic-plan-id}")
    @PreAuthorize("hasAuthority('strategic_plans.update')")
    public ResponseEntity<UUID> updateStrategicPlan(
            @PathVariable("strategic-plan-id") UUID strategicPlanId,
            @RequestBody StrategicPlanRequest request
    ) {
        return ResponseEntity.ok(strategicPlanService.updateStrategicPlan(strategicPlanId, request));
    }

    @DeleteMapping("/{strategic-plan-id}")
    @PreAuthorize("hasAuthority('strategic_plans.delete')")
    public ResponseEntity<Void> deleteStrategicPlan(@PathVariable("strategic-plan-id") UUID strategicPlanId) {
        strategicPlanService.deleteStrategicPlan(strategicPlanId);
        return ResponseEntity.noContent().build();
    }
}
