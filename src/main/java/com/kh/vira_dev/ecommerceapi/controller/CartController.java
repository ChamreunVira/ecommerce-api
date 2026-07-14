package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.AddItemRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UpdateItemRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CartResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(@Valid @RequestBody AddItemRequest request) {
        var response = cartService.addItem(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ApiResponse<CartResponse>> updateItem(@PathVariable short id, @Valid @RequestBody UpdateItemRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(cartService.updateItem(id , request)));
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> getCartItems() {
        return ResponseEntity.ok().body(ApiResponse.success(cartService.getAll()));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<CartResponse>> deleteItemFromCart(@PathVariable Short id) {
        cartService.removeItem(id);
        return ResponseEntity.ok().body(ApiResponse.success("Success to remove item from card."));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clear();
        return ResponseEntity.ok().body(ApiResponse.success("Success to clear cart."));
    }
}
