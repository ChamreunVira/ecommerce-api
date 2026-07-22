package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.ReviewRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ReviewResponse;
import com.kh.vira_dev.ecommerceapi.entity.Product;
import com.kh.vira_dev.ecommerceapi.entity.Review;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.ReviewMapper;
import com.kh.vira_dev.ecommerceapi.repository.ProductRepository;
import com.kh.vira_dev.ecommerceapi.repository.ReviewRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.ReviewService;
import com.kh.vira_dev.ecommerceapi.util.ProfanityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AuthService authService;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;

    @Override
    public ReviewResponse create(ReviewRequest request) {
        User user = authService.authenticated();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        
        if (request.getComment() != null) {
            request.setComment(ProfanityFilter.clean(request.getComment()));
        }

        Review review = reviewMapper.toEntity(request , user , product);
        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    @Override
    public ReviewResponse update(Long id, ReviewRequest request) {
        Review review = findByOrThrow(id);
        
        if (request.getComment() != null) {
            request.setComment(ProfanityFilter.clean(request.getComment()));
        }

        reviewMapper.applyToReviewFields(review, request);
        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Review review = findByOrThrow(id);
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewResponse> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public ReviewResponse findById(Long id) {
        Review review = findByOrThrow(id);
        return reviewMapper.toResponse(review);
    }

    private Review findByOrThrow(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review"));
    }
}
