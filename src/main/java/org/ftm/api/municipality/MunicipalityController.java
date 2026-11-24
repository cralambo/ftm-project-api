package org.ftm.api.municipality;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('municipalities.create')")
    public ResponseEntity<String> createMunicipality(@RequestBody MunicipalityRequest request) {
        return ResponseEntity.ok(municipalityService.createMunicipality(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('municipalities.view')")
    public ResponseEntity<PageResponse<MunicipalityResponse>> getMunicipalities(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(municipalityService.getMunicipalities(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('municipalities.view')")
    public ResponseEntity<List<ShortMunicipalityResponse>> getAllMunicipalities() {
        return ResponseEntity.ok(municipalityService.getAllMunicipalities());
    }

    @GetMapping("/{municipality_id}")
    @PreAuthorize("hasAuthority('municipalities.view')")
    public ResponseEntity<MunicipalityResponse> getMunicipalityById(@PathVariable("municipality_id") UUID municipalityId) {
        return ResponseEntity.ok(municipalityService.getMunicipalityById(municipalityId));
    }

    @GetMapping("/district/{district-id}")
    @PreAuthorize("hasAuthority('municipalities.view')")
    public ResponseEntity<List<ShortMunicipalityResponse>> getMunicipalitiesByDistrictId(@PathVariable("district-id") UUID districtId) {
        return ResponseEntity.ok(municipalityService.getMunicipalitiesByDistrictId(districtId));
    }

    @PutMapping("/update/{municipality_id}")
    @PreAuthorize("hasAuthority('municipalities.update')")
    public ResponseEntity<String> updateMunicipality(
            @PathVariable("municipality_id") UUID municipalityId,
            @RequestBody MunicipalityRequest request
    ) {
        return ResponseEntity.ok(municipalityService.updateMunicipality(municipalityId, request));
    }

    @DeleteMapping("/{municipality-id}")
    @PreAuthorize("hasAuthority('municipalities.delete')")
    public ResponseEntity<Void> deleteMunicipality(@PathVariable("municipality-id") UUID municipalityId) {
        municipalityService.deleteMunicipality(municipalityId);
        return ResponseEntity.noContent().build();
    }


}
