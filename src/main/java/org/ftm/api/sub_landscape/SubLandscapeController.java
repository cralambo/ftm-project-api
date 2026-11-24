package org.ftm.api.sub_landscape;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sub-landscapes")
public class SubLandscapeController {

    private final SubLandscapeService subLandscapeService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('sub_landscapes.create')")
    public ResponseEntity<String> createSubLandscape(@RequestBody SubLandscapeRequest request) {
        return ResponseEntity.ok(subLandscapeService.createSubLandscape(request));
    }

    @GetMapping("/{sub-landscape-id}")
    @PreAuthorize("hasAuthority('sub_landscapes.view')")
    public ResponseEntity<SubLandscapeResponse> getSubLandscapeById(@PathVariable("sub-landscape-id") UUID subLandscapeId) {
        return ResponseEntity.ok(subLandscapeService.getSubLandscapeById(subLandscapeId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sub_landscapes.view')")
    public ResponseEntity<PageResponse<SubLandscapeResponse>> getSubLandscapes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(subLandscapeService.getSubLandscapes(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('sub_landscapes.view')")
    public ResponseEntity<List<SubLandscapeResponse>> getAllSubLandscapes() {
        return ResponseEntity.ok(subLandscapeService.getAllSubLandscapes());
    }

    @GetMapping("/landscape/{landscape-id}")
    @PreAuthorize("hasAuthority('sub_landscapes.view')")
    public ResponseEntity<List<SubLandscapeResponse>> getSubLandscapesByLandscapeId(
            @PathVariable("landscape-id") UUID landscapeId
    ){
        return ResponseEntity.ok(subLandscapeService.getSubLandscapesByLandscapeId(landscapeId));
    }

    @PutMapping("/update/{sub-landscape-id}")
    @PreAuthorize("hasAuthority('sub_landscapes.update')")
    public ResponseEntity<String> updateSubLandscape(
            @PathVariable("sub-landscape-id") UUID subLandscapeId,
            @RequestBody SubLandscapeRequest request
    ) {
        return ResponseEntity.ok(subLandscapeService.updateSubLandscape(subLandscapeId, request));
    }

    @DeleteMapping("/{sub-landscape-id}")
    @PreAuthorize("hasAuthority('sub_landscapes.delete')")
    public ResponseEntity<Void> deleteSubLandscape(@PathVariable("sub-landscape-id") UUID subLandscapeId) {
        subLandscapeService.deleteSubLandscape(subLandscapeId);
        return ResponseEntity.noContent().build();
    }
}
