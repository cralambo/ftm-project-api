package org.ftm.api.department;

public class DepartmentMapper{

    public static Department mapToCreateRequest(DepartmentRequest request) {
        return Department.builder()
                .code(request.code())
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId().toString())
                .code(department.getCode())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }

    public static ShortDepartmentResponse mapToShortDepartmentResponse(Department department) {
        return ShortDepartmentResponse.builder()
                .id(department.getId().toString())
                .code(department.getCode())
                .name(department.getName())
                .build();
    }
}
