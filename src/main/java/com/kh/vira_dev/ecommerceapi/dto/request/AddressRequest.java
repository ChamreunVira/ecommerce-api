package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {

    @NotBlank(message = "full name is required.")
    private String fullName;

    @NotBlank(message = "phone number is required.")
    private String phone;

    @NotBlank(message = "order line is required.")
    private String addressLine;

    private String province;

    private String city;

    private String country;

    private String note;

    private boolean isDefault;

}
