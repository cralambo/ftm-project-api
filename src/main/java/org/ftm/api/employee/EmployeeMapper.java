package org.ftm.api.employee;

import org.ftm.api.department.DepartmentMapper;
import org.ftm.api.position.PositionMapper;

import java.time.LocalDate;

public class EmployeeMapper {

    public static Employee mapToCreateRequest(EmployeeRequest request) {
        return Employee.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .employeeNumber(request.employee_number())
                .acronym(request.acronym())
                .address(request.address())
                .personalEmail(request.personal_email())
                .professionalEmail(request.professional_email())
                .phone(request.phone())
                .hireDate(LocalDate.parse(request.hire_date()))
                .build();
    }

    public static EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId().toString())
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .employee_number(employee.getEmployeeNumber())
                .acronym(employee.getAcronym())
                .address(employee.getAddress())
                .personal_email(employee.getPersonalEmail())
                .professional_email(employee.getProfessionalEmail())
                .phone(employee.getPhone())
                .hire_date(employee.getHireDate().toString())
                .department(DepartmentMapper.mapToShortDepartmentResponse(employee.getDepartment()))
                .position(PositionMapper.mapToShortPositionResponse(employee.getPosition()))
                .build();
    }
}
