package org.ftm.api.partner_contact;

import org.ftm.api.partner.SupportPartnerMapper;
import org.ftm.api.profile.PartnerContactProfileResponse;

public class PartnerContactMapper {

    public static PartnerContact mapToCreatePartnerContact(PartnerContactRequest request) {
        return PartnerContact.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .build();
    }

    public static PartnerContactResponse mapToPartnerContactResponse(PartnerContact partnerContact) {
        return PartnerContactResponse.builder()
                .id(partnerContact.getId().toString())
                .firstname(partnerContact.getFirstname())
                .lastname(partnerContact.getLastname())
                .email(partnerContact.getEmail())
                .phone(partnerContact.getPhone())
                .address(partnerContact.getAddress())
                .supportPartner(SupportPartnerMapper.mapToShortSupportPartnerResponse(partnerContact.getSupportPartner()))
                .build();
    }

    public static ShortPartnerContactResponse mapToShortPartnerContactResponse(PartnerContact partnerContact) {
        return ShortPartnerContactResponse.builder()
                .id(partnerContact.getId().toString())
                .firstname(partnerContact.getFirstname())
                .lastname(partnerContact.getLastname())
                .build();
    }

    public static PartnerContactProfileResponse mapToPartnerContactProfileResponse(PartnerContact partnerContact) {
        return PartnerContactProfileResponse.builder()
                .id(partnerContact.getId().toString())
                .firstname(partnerContact.getFirstname())
                .lastname(partnerContact.getLastname())
                .email(partnerContact.getEmail())
                .phone(partnerContact.getPhone())
                .address(partnerContact.getAddress())
                .supportPartner(
                        SupportPartnerMapper.mapToShortSupportPartnerResponse(partnerContact.getSupportPartner())
                )
                .build();
    }
}
