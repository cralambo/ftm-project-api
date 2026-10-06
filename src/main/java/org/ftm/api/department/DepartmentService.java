package org.ftm.api.department;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public String createDepartment(DepartmentRequest request) {
        final Department department = DepartmentMapper.mapToCreateRequest(request);
        return departmentRepository.save(department).getId().toString();
    }

    public DepartmentResponse getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .map(DepartmentMapper::mapToDepartmentResponse)
                .orElseThrow(() -> new EntityNotFoundException("No department found with id: " + departmentId));
    }

    public PageResponse<DepartmentResponse> getDepartments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Department> departments = departmentRepository.findAll(pageable);
        List<DepartmentResponse> content = departments.stream()
                .map(DepartmentMapper::mapToDepartmentResponse)
                .toList();
        return PageResponse.<DepartmentResponse>builder()
                .content(content)
                .number(departments.getNumber())
                .size(departments.getSize())
                .totalElements(departments.getTotalElements())
                .totalPages(departments.getTotalPages())
                .first(departments.isFirst())
                .last(departments.isLast())
                .build();
    }

    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll(Sort.by("createdDate").descending());
        return departments.stream()
                .map(DepartmentMapper::mapToDepartmentResponse)
                .toList();
    }

    public String updateDepartment(UUID departmentId, DepartmentRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("No department found with id: " + departmentId));
        department.setCode(request.code());
        department.setName(request.name());
        department.setDescription(request.description());
        return departmentRepository.save(department).getId().toString();
    }

    public void deleteDepartment(UUID departmentId) {
        final Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("No department found with id: " + departmentId));
        departmentRepository.delete(department);
    }
}
