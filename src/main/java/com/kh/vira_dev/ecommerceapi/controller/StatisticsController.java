package com.kh.vira_dev.ecommerceapi.controller;

import com.kh.vira_dev.ecommerceapi.dto.response.StatisticsResponse;
import com.kh.vira_dev.ecommerceapi.payload.ApiResponse;
import com.kh.vira_dev.ecommerceapi.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics() {
        return ResponseEntity.ok(ApiResponse.success(statisticsService.getStatistics()));
    }

}
