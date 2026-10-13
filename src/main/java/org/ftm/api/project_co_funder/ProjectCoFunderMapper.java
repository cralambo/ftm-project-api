package org.ftm.api.project_co_funder;

public class ProjectCoFunderMapper {
    public static ShortProjectCoFunderResponse mapToShortProjectCoFunderResponse(ProjectCoFunder projectCoFunder) {
        return ShortProjectCoFunderResponse.builder()
                .id(projectCoFunder.getId().toString())
                .co_funder_name(projectCoFunder.getCoFunder().getName())
                .amount(
                        projectCoFunder.getAmount() != null
                                ? projectCoFunder.getAmount().toPlainString()
                                : null
                )
                .build();
    }
}
