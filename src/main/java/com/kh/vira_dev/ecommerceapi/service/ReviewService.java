package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.ReviewRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse create(ReviewRequest request);

    ReviewResponse update(Long id, ReviewRequest request);

    void delete(Long id);

    List<ReviewResponse> findAll();

    ReviewResponse findById(Long id);

}
