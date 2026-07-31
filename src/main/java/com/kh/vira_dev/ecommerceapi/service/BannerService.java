package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.BannerRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.BannerResponse;

import java.util.List;

public interface BannerService {

    List<BannerResponse> getAll();

    List<BannerResponse> getAllActive();

    BannerResponse getById(Long id);

    BannerResponse create(BannerRequest request);

    BannerResponse update(Long id, BannerRequest request);

    void delete(Long id);

    BannerResponse toggleActive(Long id);
}
