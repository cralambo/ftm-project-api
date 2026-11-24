package org.ftm.api.landscape;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/landscapes")
public class LandscapeController {

    private final LandscapeService landscapeService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('landscapes.create')")
    public ResponseEntity<String> createLandscape(@RequestBody LandscapeRequest request) {
        return ResponseEntity.ok(landscapeService.createLandscape(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('landscapes.view')")
    public ResponseEntity<PageResponse<LandscapeResponse>> getLandscapes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(landscapeService.getLandscapes(page, size));
    }

    @GetMapping("/{landscape-id}")
    @PreAuthorize("hasAuthority('landscapes.view')")
    public ResponseEntity<LandscapeResponse> getLandscapeById(@PathVariable("landscape-id") UUID landscapeId) {
        return ResponseEntity.ok(landscapeService.getLandscapeById(landscapeId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('landscapes.view')")
    public ResponseEntity<List<LandscapeResponse>> getAllLandscapes() {
        return ResponseEntity.ok(landscapeService.getAllLandscapes());
    }

    @PutMapping("/update/{landscape-id}")
    @PreAuthorize("hasAuthority('landscapes.update')")
    public ResponseEntity<String> updateLandscape(
            @PathVariable("landscape-id") UUID landscapeId,
            @RequestBody LandscapeRequest request
    ) {
        return ResponseEntity.ok(landscapeService.updateLandscape(landscapeId, request));
    }

    @DeleteMapping("/{landscape-id}")
    @PreAuthorize("hasAuthority('landscapes.delete')")
    public ResponseEntity<Void> deleteLandscape(@PathVariable("landscape-id") UUID landscapeId) {
        landscapeService.deleteLandscape(landscapeId);
        return ResponseEntity.noContent().build();
    }
}
