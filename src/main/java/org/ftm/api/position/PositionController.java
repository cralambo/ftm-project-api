package org.ftm.api.position;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;


    @PostMapping("/create")
    public ResponseEntity<String> createPosition(@RequestBody PositionRequest request) {
        return ResponseEntity.ok(positionService.createPosition(request));
    }

    @GetMapping("/{position-id}")
    public ResponseEntity<PositionResponse> getPositionById(@PathVariable("position-id") UUID positionId) {
        return ResponseEntity.ok(positionService.getPositionById(positionId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PositionResponse>> getPositions(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(positionService.getPositions(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PositionResponse>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @PutMapping("/update/{position-id}")
    public ResponseEntity<String> updatePosition(
            @PathVariable("position-id") UUID positionId,
            @RequestBody PositionRequest request
    ) {
        return ResponseEntity.ok(positionService.updatePosition(positionId, request));
    }

    @DeleteMapping("/{position-id}")
    public ResponseEntity<Void> deletePosition(@PathVariable("position-id") UUID positionId) {
        positionService.deletePosition(positionId);
        return ResponseEntity.noContent().build();
    }
}
