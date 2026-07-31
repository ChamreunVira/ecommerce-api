package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.ProductRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ProductResponse;
import com.kh.vira_dev.ecommerceapi.entity.Product;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.ProductMapper;
import com.kh.vira_dev.ecommerceapi.repository.ProductRepository;
import com.kh.vira_dev.ecommerceapi.service.ProductService;
import com.kh.vira_dev.ecommerceapi.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Value("${uploads.dir}")
    private String UPLOAD_DIR;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {

        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);
        log.info("Product created: {}", saved);
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findByOrThrow(id);
        productMapper.applyToProductFields(product , request);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Product product = findByOrThrow(id);
        product.getImage().forEach(image -> {
            try {
                Path imagePath = Paths.get(UPLOAD_DIR).resolve(image);
                Files.deleteIfExists(imagePath);
            }catch (IOException ex) {
                log.info("Could not delete image {}", image);
                throw new ResourceNotFoundException("image not found");
            }
});
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = findByOrThrow(id);
        return productMapper.toResponse(product);
    }

    @Override
    @Cacheable("products")
    public List<ProductResponse> getAll() {
        List<Product> products = productRepository.findAll();
        log.info("Product retrieved: {}", products);
        return productMapper.toResponseList((products));
    }

    @Override
    public List<ProductResponse> filterPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        Specification<Product> productSpec = ProductSpecification.hasPriceBetween(minPrice , maxPrice);
        List<Product> productList = productRepository.findAll(productSpec);
        return productMapper.toResponseList((productList));
    }

    private Product findByOrThrow(Long id) {
        log.info("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
    }
}
