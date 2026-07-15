package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {

    private Long addressId;

    private String fullName;

    private String phone;

    private String province;

    private String addressLine;

    private String city;

    private String country;

    private String note;

    private boolean isDefault;

}
