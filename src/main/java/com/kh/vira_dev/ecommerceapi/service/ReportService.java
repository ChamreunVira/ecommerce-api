package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.response.TopProductResponse;
import com.kh.vira_dev.ecommerceapi.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderItemRepository orderItemRepository;

    public List<TopProductResponse> topProduct() {
        return orderItemRepository.findTop5Product();
    }



}
