package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse update(Long id , UserRequest request);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    void updateStatus(Long id);

}
