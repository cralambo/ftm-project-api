package org.ftm.api.employee;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ftm.api.common.PageResponse;
import org.ftm.api.department.Department;
import org.ftm.api.department.DepartmentRepository;
import org.ftm.api.position.Position;
import org.ftm.api.position.PositionRepository;
import org.ftm.api.user.User;
import org.ftm.api.user.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final UserAccountService userAccountService;

    @Transactional
    public String createEmployee(EmployeeRequest request) {
        Employee employee = EmployeeMapper.mapToCreateRequest(request);

        final Department department = departmentRepository.findById(UUID.fromString(request.department_id()))
                .orElseThrow(() -> new EntityNotFoundException("No department found with id: " + request.department_id()));

        final Position position = positionRepository.findById(UUID.fromString(request.position_id()))
                .orElseThrow(() -> new EntityNotFoundException("No position found with id: " + request.position_id()));

        if (employeeRepository.existsByAcronym(request.acronym())) {
            throw new IllegalArgumentException("");
        }

        if (employeeRepository.existsByEmployeeNumber(request.employee_number())) {
            throw new IllegalArgumentException("");
        }

        employee.setDepartment(department);
        employee.setPosition(position);

        // create user account
        final User user = userAccountService.createEmployeeAccount(
                request.professional_email(),
                request.firstname(),
                request.lastname());

        employee.setUser(user);

        return employeeRepository.save(employee).getId().toString();
    }

    public EmployeeResponse getEmployeeById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .map(EmployeeMapper::mapToEmployeeResponse)
                .orElseThrow(() -> new EntityNotFoundException("No employee found with id: " + employeeId));
    }

    public PageResponse<EmployeeResponse> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<EmployeeResponse> content = employees.stream()
                .map(EmployeeMapper::mapToEmployeeResponse)
                .toList();
        return PageResponse.<EmployeeResponse>builder()
                .content(content)
                .number(employees.getNumber())
                .size(employees.getSize())
                .totalElements(employees.getTotalElements())
                .totalPages(employees.getTotalPages())
                .first(employees.isFirst())
                .last(employees.isLast())
                .build();
    }

    public String updateEmployee(UUID employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("No employee found with id: " + employeeId));

        final Department department = departmentRepository.findById(UUID.fromString(request.department_id()))
                .orElseThrow(() -> new EntityNotFoundException("No department found with id: " + request.department_id()));

        final Position position = positionRepository.findById(UUID.fromString(request.position_id()))
                .orElseThrow(() -> new EntityNotFoundException("No position found with id: " + request.position_id()));

        employee.setFirstname(request.firstname());
        employee.setLastname(request.lastname());
        employee.setEmployeeNumber(request.employee_number());
        employee.setAcronym(request.acronym());
        employee.setAddress(request.address());
        employee.setPersonalEmail(request.personal_email());
        employee.setProfessionalEmail(request.professional_email());
        employee.setPhone(request.phone());
        employee.setHireDate(LocalDate.parse(request.hire_date()));
        employee.setDepartment(department);
        employee.setPosition(position);

        return employeeRepository.save(employee).getId().toString();
    }

    public void deleteEmployee(UUID employeeId) {
        final Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("No employee found with id: " + employeeId));
        employeeRepository.delete(employee);
    }
}
