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
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required.")
    private String oldPassword;

    @NotBlank(message = "New password is required.")
    private String newPassword;

}
