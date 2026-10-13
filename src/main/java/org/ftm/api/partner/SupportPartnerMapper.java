package org.ftm.api.partner;

public class SupportPartnerMapper {

    public static SupportPartner mapToCreatePartner(SupportPartnerRequest request) {
        return SupportPartner.builder()
                .name(request.name())
                .address(request.address())
                .phone(request.phone())
                .email(request.email())
                .build();
    }

    public static SupportPartnerResponse mapToPartnerResponse(SupportPartner supportPartner) {
        return SupportPartnerResponse.builder()
                .id(supportPartner.getId().toString())
                .name(supportPartner.getName())
                .address(supportPartner.getAddress())
                .phone(supportPartner.getPhone())
                .email(supportPartner.getEmail())
                .build();
    }

    public static ShortSupportPartnerResponse mapToShortSupportPartnerResponse(SupportPartner supportPartner) {
        return ShortSupportPartnerResponse.builder()
                .id(supportPartner.getId().toString())
                .name(supportPartner.getName())
                .build();
    }
}
