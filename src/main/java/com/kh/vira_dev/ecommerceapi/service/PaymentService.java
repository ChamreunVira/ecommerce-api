package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.GenerateQrRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.KhqrResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PaymentResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.PaymentStatusResponse;

import java.util.List;

public interface PaymentService {

    List<PaymentResponse> getAll();

    KhqrResponse generateQr(GenerateQrRequest request);

    PaymentStatusResponse checkStatus(String transactionId);

    //feature
    void handleWebhook();

    PaymentResponse getByOrder(Short orderId);
}
