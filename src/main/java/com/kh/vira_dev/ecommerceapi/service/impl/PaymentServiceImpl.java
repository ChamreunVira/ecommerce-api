package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.config.BakongConfig;
import com.kh.vira_dev.ecommerceapi.dto.request.BakongApiRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.GenerateQrRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.*;
import com.kh.vira_dev.ecommerceapi.entity.Order;
import com.kh.vira_dev.ecommerceapi.entity.Payment;
import com.kh.vira_dev.ecommerceapi.enums.CurrencyType;
import com.kh.vira_dev.ecommerceapi.enums.OrderStatus;
import com.kh.vira_dev.ecommerceapi.enums.PaymentStatus;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.repository.OrderRepository;
import com.kh.vira_dev.ecommerceapi.repository.PaymentRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.BakongService;
import com.kh.vira_dev.ecommerceapi.service.PaymentService;

import com.kh.vira_dev.ecommerceapi.service.TelegramNotificationService;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends BakongConfig implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final BakongService bakongService;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    public List<PaymentResponse> getAll() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::toPaymentResponse)
                .toList();
    }

    @Override
    public KhqrResponse generateQr(GenerateQrRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order"));

        Payment existing = order.getPayment();
        if (existing != null && existing.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Order has already been paid.");
        }
        if (existing != null && existing.getPaymentStatus() == PaymentStatus.PENDING) {
            return toKhqrResponse(existing);
        }

        String transactionId = order.getOrderCode();
        BakongApiRequest bakongApiRequest = new BakongApiRequest();
        applyBakongApiFields(bakongApiRequest , transactionId , order);
        BakongApiResponse response = bakongService.generateQrCode(bakongApiRequest);

        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);
        Payment payment = builderPayment(response, transactionId, order);
        Payment saved = paymentRepository.save(payment);

        return toKhqrResponse(saved);
    }

    @Override
    public PaymentStatusResponse checkStatus(String transactionId) {

        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment"));

        CheckTransactionResponse response = bakongService.checkTransactionByMd5(payment.getMd5());
        if(response.getStatusCode() == 0) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setPaidAt(Instant.now());
            payment.setExpiresAt(null);
            paymentRepository.save(payment);

            Order order = payment.getOrder();
            order.setOrderStatus(OrderStatus.PROCESSING);

            telegramNotificationService.sendPaymentSuccessAlert(payment.getOrder() , payment.getAmount());

            orderRepository.save(order);
        }

        return toPaymentStatusResponse(payment);
    }

    @Override
    public void handleWebhook() {
    }

    @Override
    public PaymentResponse getByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));
        Payment payment = order.getPayment();
        if (payment == null) {
            throw new ResourceNotFoundException("Payment");
        }
        return toPaymentResponse(payment);
    }

    private Payment builderPayment(BakongApiResponse response, String transactionId, Order order) {
        Payment payment = new Payment();
        payment.setQrString(response.getQrString());
        payment.setMd5(response.getMd5());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(transactionId);
        payment.setMethod(order.getPaymentMethod());
        payment.setDeepLink("feature-future");
        payment.setCurrency(CurrencyType.USD);
        payment.setAmount(order.getTotalAmount());
        payment.setWebhookPayload("feature-future");
        payment.setExpiresAt(Instant.now().plusSeconds(900));
        payment.setOrder(order);
        return payment;
    }

    private void applyBakongApiFields(BakongApiRequest bakongApiRequest , String transactionId, Order order) {
        bakongApiRequest.setTransactionId(transactionId);
        bakongApiRequest.setCurrency(KHQRCurrency.USD);
        bakongApiRequest.setAmount(order.getTotalAmount().doubleValue());
    }

    private KhqrResponse toKhqrResponse(Payment payment) {
        Order order = payment.getOrder();
        return KhqrResponse.builder()
                .paymentId(payment.getId())
                .orderId(order.getId())
                .transactionId(payment.getTransactionId())
                .status(payment.getPaymentStatus())
                .amount(order.getTotalAmount())
                .qrString(payment.getQrString())
                .deeplink(payment.getDeepLink())
                .currency(payment.getCurrency())
                .expiresAt(payment.getExpiresAt())
                .build();
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .orderCode(payment.getOrder().getOrderCode())
                .customer(payment.getOrder().getShippingAddressSnapshot().getFullName())
                .transactionId(payment.getTransactionId())
                .status(payment.getPaymentStatus())
                .method(payment.getMethod())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .qrString(payment.getQrString())
                .deeplink(payment.getDeepLink())
                .expiresAt(payment.getExpiresAt())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    private PaymentStatusResponse toPaymentStatusResponse(Payment payment) {
        return PaymentStatusResponse.builder()
                .transactionId(payment.getTransactionId())
                .orderId(payment.getOrder().getId())
                .status(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .expiresAt(payment.getExpiresAt())
                .paidAt(payment.getPaidAt())
                .build();
    }

}
