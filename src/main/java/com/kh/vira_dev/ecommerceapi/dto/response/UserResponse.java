package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private int id;

    private String fullName;

    private String email;

    private String password;

    private String refreshToken;

    private String accessToken;

    private List<String> roles;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
