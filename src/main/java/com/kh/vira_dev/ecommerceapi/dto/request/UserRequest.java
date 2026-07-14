package com.kh.vira_dev.ecommerceapi.dto.request;

import com.kh.vira_dev.ecommerceapi.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "full name is required.")
    private String fullName;

    @NotBlank(message = "email is required.")
    private String email;

    @NotBlank(message = "password is required.")
    private String password;

    private Set<Role> roles;

}
