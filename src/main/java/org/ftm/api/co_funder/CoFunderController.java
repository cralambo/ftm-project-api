package org.ftm.api.co_funder;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/co-funders")
@RequiredArgsConstructor
public class CoFunderController {

    private final CoFunderService coFunderService;

    @PostMapping("/create")
    public ResponseEntity<String> createCoFunder(@RequestBody CoFunderRequest request) {
        return ResponseEntity.ok(coFunderService.createCoFunder(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CoFunderResponse>> getCoFunders(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(coFunderService.getCoFunders(page, size));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CoFunderResponse>> getAllCoFunders() {
        return ResponseEntity.ok(coFunderService.getAllCoFunders());
    }

    @GetMapping("/{co-funder-id}")
    public ResponseEntity<CoFunderResponse> getCoFunderByID(@PathVariable("co-funder-id") UUID coFunderId) {
        return ResponseEntity.ok(coFunderService.getCoFunderById(coFunderId));
    }

    @PutMapping("/update/{co-funder-id}")
    public ResponseEntity<String> updateCoFunder(
            @PathVariable("co-funder-id") UUID coFunderId,
            @RequestBody CoFunderRequest request
    ) {
        return ResponseEntity.ok(coFunderService.updateCoFunder(coFunderId, request));
    }

    @DeleteMapping("/{co-funder-id}")
    public ResponseEntity<Void> deleteCoFunder(@PathVariable("co-funder-id") UUID coFunderId) {
        coFunderService.deleteCoFunder(coFunderId);
        return ResponseEntity.noContent().build();
    }
}
