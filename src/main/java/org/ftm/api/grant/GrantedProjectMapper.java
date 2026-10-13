package org.ftm.api.grant;

public class GrantedProjectMapper {

    public static GrantedProjectResponse mapToGrantedProjectResponse(GrantedProject grantedProject) {
        return GrantedProjectResponse.builder()
                .id(grantedProject.getId().toString())
                .grant_date(grantedProject.getGrantDate() != null ? grantedProject.getGrantDate().toString() : null)
                .grant_amount(grantedProject.getGrantAmount().toPlainString())
                .build();
    }
}
