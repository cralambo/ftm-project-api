package org.ftm.api.department;

import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('departments.create')")
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.createDepartment(request));
    }

    @GetMapping("/{department-id}")
    @PreAuthorize("hasAuthority('departments.view')")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable("department-id") UUID departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('departments.view')")
    public ResponseEntity<PageResponse<DepartmentResponse>> getDepartments(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "100", required = false) int size
    ) {
        return ResponseEntity.ok(departmentService.getDepartments(page, size));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('departments.view')")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/update/{department-id}")
    @PreAuthorize("hasAuthority('departments.update')")
    public ResponseEntity<String> updateDepartment(
            @PathVariable("department-id") UUID departmentId,
            @RequestBody DepartmentRequest request
    ) {
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, request));
    }

    @DeleteMapping("/{department-id}")
    @PreAuthorize("hasAuthority('departments.delete')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("department-id") UUID departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
}
