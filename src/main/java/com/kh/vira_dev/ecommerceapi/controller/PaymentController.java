package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.request.GenerateQrRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.KhqrResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PaymentResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PaymentStatusResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAll() {
        List<PaymentResponse> response = paymentService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/khqr/generate")
    public ResponseEntity<ApiResponse<KhqrResponse>> generateQrCode(@RequestBody GenerateQrRequest req) {
        var response = paymentService.generateQr(req);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getOrder(@PathVariable Short orderId) {
        var response = paymentService.getByOrder(orderId);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/khqr/{transactionId}/status")
    public ResponseEntity<ApiResponse<PaymentStatusResponse>> getPaymentStatus(@PathVariable String transactionId) {
        var response = paymentService.checkStatus(transactionId);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
