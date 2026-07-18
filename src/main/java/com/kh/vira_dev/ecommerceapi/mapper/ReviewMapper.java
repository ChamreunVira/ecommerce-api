package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.ReviewRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ReviewResponse;
import com.kh.vira_dev.ecommerceapi.entity.Product;
import com.kh.vira_dev.ecommerceapi.entity.Review;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.enums.ReviewStatus;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequest request, User user, Product product) {
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(ReviewStatus.PUBLISHED);
        review.setCustomer(user);
        review.setProduct(product);
        return review;
    }

    public ReviewResponse toResponse(Review review) {
        ReviewResponse.CustomerInfo customer = null;
        if (review.getCustomer() != null) {
            customer = ReviewResponse.CustomerInfo.builder()
                    .id(review.getCustomer().getId())
                    .name(review.getCustomer().getFullName())
                    .build();
        }

        ReviewResponse.ProductInfo product = null;
        if (review.getProduct() != null) {
            product = ReviewResponse.ProductInfo.builder()
                    .id(review.getProduct().getId())
                    .name(review.getProduct().getName())
                    .build();
        }

        return ReviewResponse.builder()
                .id(review.getId())
                .customer(customer)
                .product(product)
                .rating(review.getRating())
                .comment(review.getComment())
                .status(review.getStatus() != null ? review.getStatus().name() : null)
                .createdAt(review.getCreatedAt())
                .build();
    }

    public void applyToReviewFields(Review review, ReviewRequest reviewRequest) {
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
    }

}
