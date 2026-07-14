package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.response.RefreshTokenResponse;
import com.kh.vira_dev.ecommerceapi.entity.RefreshToken;
import com.kh.vira_dev.ecommerceapi.entity.User;

public interface RefreshTokenService {

    RefreshToken refresh(User user);

    RefreshTokenResponse verify(String token);

}
