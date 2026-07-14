package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.ProductRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ProductResponse;
import com.kh.vira_dev.ecommerceapi.entity.Category;
import com.kh.vira_dev.ecommerceapi.entity.Product;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.repository.CategoryRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final AuthService authService;
    private final CategoryRepository categoryRepository;
    private final FileUploadUtils fileUploadUtils;

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        applyToProductFields(product , request);
        return product;
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse
                .builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .name(product.getName())
                .userId(product.getUser().getId())
                .username(product.getUser().getFullName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscountRate())
                .qty(product.getQty())
                .images(product.getImage())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    public void applyToProductFields(Product product , ProductRequest request) {

        if(request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category"));
            product.setCategory(category);
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountRate(request.getDiscount());
        product.setQty(request.getQty());

        if(request.getImages() != null && !request.getImages().isEmpty()) {
            product.setImage(toProductImages(request.getImages()));
        }
        product.setUser(authService.authenticated());
    }

    private List<String> toProductImages(List<MultipartFile> imagesReq) {
        return imagesReq
                .stream()
                .map(fileUploadUtils::upload)
                .toList();
    }

}
