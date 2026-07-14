package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;
import com.kh.vira_dev.ecommerceapi.entity.RefreshToken;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.exception.DuplicateResourceException;
import com.kh.vira_dev.ecommerceapi.jwt.JwtService;
import com.kh.vira_dev.ecommerceapi.mapper.UserMapper;
import com.kh.vira_dev.ecommerceapi.repository.UserRepository;
import com.kh.vira_dev.ecommerceapi.service.RefreshTokenService;
import com.kh.vira_dev.ecommerceapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("email");
        }

        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        log.info("User created: {}", saved);

        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.refresh(user);
        user.setRefreshToken(refreshToken);

        return toResponse(user , token);
    }

    @Override
    public UserResponse update(int id, UserRequest request) {
        User user = findByOrThrow(id);
        userMapper.applyToUserField(user , request);
        User saved = userRepository.save(user);
        log.info("User updated: {}", saved);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse getById(int id) {
        User user = findByOrThrow(id);
        log.info("User getById: {}", user);
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        log.info("Users found: {}", users);
        return userMapper.toResponseList(users);
    }

    @Override
    public void updateStatus(int id) {
        User user = findByOrThrow(id);
        user.setStatus(!user.isStatus());
        log.info("User updated: {}", user);
        userRepository.save(user);
    }

    private User findByOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user , String token) {
        UserResponse response = userMapper.toResponse(user);
        response.setRefreshToken(user.getRefreshToken().getToken());
        response.setAccessToken(token);
        return response;
    }
}
