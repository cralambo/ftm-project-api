package org.ftm.api.sub_landscape;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sub-landscapes")
public class SubLandscapeController {

    private final SubLandscapeService subLandscapeService;

    @PostMapping("/create")
    public ResponseEntity<String> createSubLandscape(@RequestBody SubLandscapeRequest request) {
        return ResponseEntity.ok(subLandscapeService.createSubLandscape(request));
    }

    @GetMapping("/{sub-landscape-id}")
    public ResponseEntity<SubLandscapeResponse> getSubLandscapeById(@PathVariable("sub-landscape-id") UUID subLandscapeId) {
        return ResponseEntity.ok(subLandscapeService.getSubLandscapeById(subLandscapeId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SubLandscapeResponse>> getSubLandscapes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(subLandscapeService.getSubLandscapes(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubLandscapeResponse>> getAllSubLandscapes() {
        return ResponseEntity.ok(subLandscapeService.getAllSubLandscapes());
    }

    @GetMapping("/landscape/{landscape-id}")
    public ResponseEntity<List<SubLandscapeResponse>> getSubLandscapesByLandscapeId(
            @PathVariable("landscape-id") UUID landscapeId
    ){
        return ResponseEntity.ok(subLandscapeService.getSubLandscapesByLandscapeId(landscapeId));
    }

    @PutMapping("/update/{sub-landscape-id}")
    public ResponseEntity<String> updateSubLandscape(
            @PathVariable("sub-landscape-id") UUID subLandscapeId,
            @RequestBody SubLandscapeRequest request
    ) {
        return ResponseEntity.ok(subLandscapeService.updateSubLandscape(subLandscapeId, request));
    }

    @DeleteMapping("/{sub-landscape-id}")
    public ResponseEntity<Void> deleteSubLandscape(@PathVariable("sub-landscape-id") UUID subLandscapeId) {
        subLandscapeService.deleteSubLandscape(subLandscapeId);
        return ResponseEntity.noContent().build();
    }
}
