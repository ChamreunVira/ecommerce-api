package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.annotation.Audit;
import com.kh.vira_dev.ecommerceapi.dto.request.ProductRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.ProductResponse;
import com.kh.vira_dev.ecommerceapi.enums.AuditAction;
import com.kh.vira_dev.ecommerceapi.enums.AuditModule;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.ProductService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
        var response = productService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        var response = productService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> filterPrice(@RequestParam BigDecimal minPrice , @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(ApiResponse.success(productService.filterPrice(minPrice, maxPrice)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,  produces = MediaType.APPLICATION_JSON_VALUE)
    @Audit(action = AuditAction.CREATE, module = AuditModule.PRODUCT)
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @ModelAttribute ProductRequest request) {
        var response = productService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping(path = "/{id}" , consumes =  MediaType.MULTIPART_FORM_DATA_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @Audit(action = AuditAction.UPDATE, module = AuditModule.PRODUCT, entityIdParam = "id")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Long id, @Valid @ModelAttribute ProductRequest request) {
        var response = productService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Audit(action = AuditAction.DELETE, module = AuditModule.PRODUCT, entityIdParam = "id")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
