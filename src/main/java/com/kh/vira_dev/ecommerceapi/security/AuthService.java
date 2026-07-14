package com.kh.vira_dev.ecommerceapi.security;

import com.kh.vira_dev.ecommerceapi.dto.request.AuthRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.ChangePasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.SendOtpRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.OtpResponse;
import com.kh.vira_dev.ecommerceapi.dto.request.ResetPasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.VerifyOtpResponse;
import com.kh.vira_dev.ecommerceapi.entity.User;

public interface AuthService {

    UserResponse singIn(AuthRequest request);

    User authenticated();

    UserResponse me();

    UserResponse changePassword(ChangePasswordRequest request);

    UserResponse resetPassword(ResetPasswordRequest request);

    VerifyOtpResponse verifyOtp(String email, String otp);

    OtpResponse sendResetOtp(SendOtpRequest request);

}
