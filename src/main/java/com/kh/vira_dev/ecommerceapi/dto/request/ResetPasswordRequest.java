package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {

    @NotBlank(message = "New password is required.")
    @Size(min = 6 , max = 255 , message = "New password must be at least grater than 6 character.")
    private String newPassword;

    @NotBlank(message = "OTP is required.")
    private String otp;
}

