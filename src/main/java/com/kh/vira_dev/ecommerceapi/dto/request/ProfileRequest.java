package com.kh.vira_dev.ecommerceapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {

    private String fullName;

    private String bio;

    private String phoneNumber;

    private String dateOfBirth;

}
