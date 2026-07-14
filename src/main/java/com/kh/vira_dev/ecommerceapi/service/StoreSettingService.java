package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.StoreSettingRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.StoreSettingResponse;

import java.util.List;

public interface StoreSettingService {

    StoreSettingResponse create(StoreSettingRequest request);

    StoreSettingResponse update(Long id, StoreSettingRequest request);

    void delete(Long id);

    List<StoreSettingResponse> getAll();

    StoreSettingResponse getById(Long id);

}
