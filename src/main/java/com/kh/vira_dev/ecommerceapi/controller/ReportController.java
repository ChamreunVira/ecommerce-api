package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.response.RevenueByMonthResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.TopProductResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.OrderService;
import com.kh.vira_dev.ecommerceapi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final OrderService orderService;
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RevenueByMonthResponse>>> reportAllMonth() {
        List<RevenueByMonthResponse> response = orderService.revenueByMonth();
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/top-product")
    public ResponseEntity<ApiResponse<List<TopProductResponse>>> topCategories() {
        List<TopProductResponse> responses = reportService.topProduct();
        return ResponseEntity.ok().body(ApiResponse.success(responses));
    }

}
