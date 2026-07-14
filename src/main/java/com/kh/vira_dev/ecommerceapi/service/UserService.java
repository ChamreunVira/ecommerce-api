package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.UserRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse update(int id , UserRequest request);

    UserResponse getById(int id);

    List<UserResponse> getAll();

    void updateStatus(int id);

}
