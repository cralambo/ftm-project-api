package org.ftm.api.district;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/districts")
public class DistrictController {

    private final DistrictService districtService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('districts.create')")
    public ResponseEntity<String> createDistrict(@RequestBody DistrictRequest request) {
        return ResponseEntity.ok(districtService.createDistrict(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('districts.view')")
    public ResponseEntity<PageResponse<DistrictResponse>> getDistricts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(districtService.getDistricts(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('districts.view')")
    public ResponseEntity<List<ShortDistrictResponse>> getAllDistricts() {
        return ResponseEntity.ok(districtService.getAllDistricts());
    }

    @GetMapping("/region/{region-id}")
    @PreAuthorize("hasAuthority('districts.view')")
    public ResponseEntity<List<ShortDistrictResponse>> getDistrictsByRegionId(@PathVariable("region-id") UUID regionId) {
        return ResponseEntity.ok(districtService.getDistrictsByRegionId(regionId));
    }

    @GetMapping("/{district-id}")
    @PreAuthorize("hasAuthority('districts.view')")
    public ResponseEntity<DistrictResponse> getDistrictById(@PathVariable("district-id") UUID districtId) {
        return ResponseEntity.ok(districtService.getDistrictById(districtId));
    }

    @PutMapping("/update/{district-id}")
    @PreAuthorize("hasAuthority('districts.update')")
    public ResponseEntity<String> updateDistrict(
            @PathVariable("district-id") UUID districtId,
            @RequestBody DistrictRequest request
    ) {
        return ResponseEntity.ok(districtService.updateDistrict(districtId, request));
    }

    @DeleteMapping("/{district-id}")
    @PreAuthorize("hasAuthority('districts.delete')")
    public ResponseEntity<Void> deleteDistrict(@PathVariable("district-id") UUID districtId) {
        districtService.deleteDistrict(districtId);
        return ResponseEntity.noContent().build();
    }
}
