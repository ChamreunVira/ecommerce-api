package com.kh.vira_dev.ecommerceapi.security.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.AuthRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.ChangePasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.ResetPasswordRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.SendOtpRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.OtpResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.VerifyOtpResponse;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import com.kh.vira_dev.ecommerceapi.mapper.UserMapper;
import com.kh.vira_dev.ecommerceapi.repository.UserRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.EmailService;
import com.kh.vira_dev.ecommerceapi.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserResponse singIn(AuthRequest request) {

        Authentication authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        authenticationManager.authenticate(authToken);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        UserResponse response = userMapper.toResponse(user);
        response.setAccessToken(jwtService.generateToken(user.getEmail()));
        return response;
    }

    @Override
    public User authenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            return userRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + auth.getName()));
        }
        throw new UsernameNotFoundException("User not found.");
    }

    @Override
    public UserResponse me() {
        User user = this.authenticated();
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest request) {
        User user = this.authenticated();
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Password don't match");
        }

        String newPassBcrypt = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPassBcrypt);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse resetPassword(ResetPasswordRequest request) {
        User user = this.authenticated();
        validateOtp(user , request.getOtp());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetOptExpireAt(0L);
        user.setResetOpt(null);
        User savedUser = userRepository.save(user);
        return userMapper
                .toResponse(savedUser);
    }

    @Override
    public VerifyOtpResponse verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        validateOtp(user , otp);
        validateOtpExpire(user);
        return VerifyOtpResponse
                .builder()
                .message("Verify OTP 6 digit successfully.")
                .email(user.getEmail())
                .build();
    }

    @Override
    public OtpResponse sendResetOtp(SendOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email"));

        String otpCode = Utils.generateOtpCode();
        user.setResetOpt(otpCode);
        Long expireDate = System.currentTimeMillis() + (60 * 5 * 1000);
        user.setResetOptExpireAt(expireDate);
        emailService.sendOtp(user.getEmail() , otpCode);
        User savedUser = userRepository.save(user);
        return OtpResponse.builder()
                .opt(savedUser.getResetOpt())
                .build();
    }

    private void validateOtp(User user, String otp) {
        if(user.getResetOpt() == null || !user.getResetOpt().equals(otp)) {
            throw new IllegalArgumentException("OTP doesn't match.");
        }
    }

    private void validateOtpExpire(User user) {
        if(user.getResetOptExpireAt() < System.currentTimeMillis()) {
            throw new IllegalArgumentException("OTP is expiration.");
        }
    }


}
