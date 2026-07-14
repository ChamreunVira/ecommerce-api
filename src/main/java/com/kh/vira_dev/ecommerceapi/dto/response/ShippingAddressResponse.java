package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingAddressResponse {

    private String fullName;

    private String phone;

    private String addressLine;

    private String city;

    private String country;

}
