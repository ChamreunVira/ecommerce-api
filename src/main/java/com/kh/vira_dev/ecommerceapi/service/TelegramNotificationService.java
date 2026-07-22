package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationService {

    private final RestTemplate telegramRestTemplate = new RestTemplate();
    private final Queue<String> failedAlerts = new ConcurrentLinkedQueue<>();

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    @Async
    public void sendPaymentSuccessAlert(Order order, BigDecimal amount) {
        String text = String.format("""
            🎉 *Payment Received Successfully*

            💳 *Payment Information*
            • Status: ✅ PAID
            • Amount: *%s KHR*
            • Method: `%s`

            📦 *Order Information*
            • Order Code: `%s`
            • Order ID: `%s`
            • Items: *%d*
            • Total: *%s KHR*

            👤 *Customer*
            • Name: %s
            • Email: `%s`

            📍 *Shipping*
            • Recipient: %s
            • Phone: `%s`

            🕒 *Payment Time*
            `%s`

            🔗 *Quick Actions*
            • View Order: http://localhost:3000/orders/%s
            • Admin Panel: http://localhost:3000/admin/orders/%s

            🚀 Payment completed successfully.
            """,
                amount,
                order.getPaymentMethod(),
                order.getOrderCode(),
                order.getId(),
                order.getOrderItems().size(),
                order.getTotalAmount(),
                order.getUser().getFullName(),
                order.getUser().getEmail(),
                order.getShippingAddressSnapshot().getFullName(),
                order.getShippingAddressSnapshot().getPhone(),
                LocalDateTime.now(),
                order.getOrderCode(),
                order.getOrderCode()
        );

        sendWithRetry(text, 3);
    }

    private void sendWithRetry(String text, int attemptsLeft) {
        try {
            send(text);
        } catch (ResourceAccessException e) {
            if (attemptsLeft > 1) {
                sleep(2000);
                sendWithRetry(text, attemptsLeft - 1);
            } else {
                failedAlerts.offer(text); // just hold it in memory
                log.error("Telegram alert queued in memory after exhausting retries", e);
            }
        } catch (Exception e) {
            log.error("Telegram send failed (non-network)", e);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void flushFailedAlerts() {
        String text;
        while ((text = failedAlerts.poll()) != null) {
            try {
                send(text);
            } catch (Exception e) {
                failedAlerts.offer(text); // put it back, try again next cycle
                break;
            }
        }
    }

    private void send(String text) {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
        Map<String, Object> payload = Map.of("chat_id", chatId, "text", text, "parse_mode", "Markdown");
        telegramRestTemplate.postForEntity(url, payload, String.class);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}