package org.ftm.api.region;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<String> createRegion(@RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.createRegion(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<RegionResponse>> getRegions(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(regionService.getRegions(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RegionResponse>> getAllRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/{region-id}")
    public ResponseEntity<RegionResponse> getRegionById(@PathVariable("region-id") UUID regionId) {
        return ResponseEntity.ok(regionService.getRegionById(regionId));
    }

    @PutMapping("/update/{region-id}")
    public ResponseEntity<String> updateRegion(
            @PathVariable("region-id") UUID regionId,
            @RequestBody RegionRequest request
    ) {
        return ResponseEntity.ok(regionService.updateRegion(regionId, request));
    }

    @DeleteMapping("/{region-id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable("region-id") UUID regionId) {
        regionService.deleteRegion(regionId);
        return ResponseEntity.noContent().build();
    }
}
