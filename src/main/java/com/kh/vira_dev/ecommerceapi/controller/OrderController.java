package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.CheckoutRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UpdateOrderStatusRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.*;
import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.CheckoutOrchestrator;
import com.kh.vira_dev.ecommerceapi.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CheckoutOrchestrator checkoutOrchestrator;

    @PostMapping
    public ResponseEntity<ApiResponse<CheckoutResponse>> create(@Valid @RequestBody CheckoutRequest request) {
        CheckoutResponse response = checkoutOrchestrator.checkout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> allOrder() {
        var response = orderService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrders(@RequestParam OrderStatus status) {
        var response = orderService.getMyOrders(status);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<OrderCancelResponse>> cancelOrder(@PathVariable Long id) {
        var response = orderService.cancel(id);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderStatusUpdateResponse>> updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        var response = orderService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable Long id) {
        var response = orderService.getDetails(id);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<RecentOrderResponse>>> getRecentOrders() {
        var response = orderService.recent();
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
