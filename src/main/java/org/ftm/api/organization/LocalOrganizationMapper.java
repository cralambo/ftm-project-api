package org.ftm.api.organization;

public class LocalOrganizationMapper {

    public static LocalOrganization mapToCreateOrganizationRequest(LocalOrganizationRequest request) {
        return LocalOrganization.builder()
                .name(request.name())
                .address(request.address())
                .phone(request.phone())
                .email(request.email())
                .build();
    }

    public static LocalOrganizationResponse mapToOrganizationResponse(LocalOrganization localOrganization) {
        return LocalOrganizationResponse.builder()
                .id(localOrganization.getId().toString())
                .name(localOrganization.getName())
                .address(localOrganization.getAddress())
                .phone(localOrganization.getPhone())
                .email(localOrganization.getEmail())
                .build();
    }

    public static ShortLocalOrganizationResponse mapToShortLocalOrganization(LocalOrganization localOrganization) {
        return ShortLocalOrganizationResponse.builder()
                .id(localOrganization.getId().toString())
                .name(localOrganization.getName())
                .build();
    }
}
