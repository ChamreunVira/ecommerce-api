package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.StoreSettingRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.StoreSettingResponse;
import com.kh.vira_dev.ecommerceapi.entity.StoreSetting;
import org.springframework.stereotype.Component;

@Component
public class StoreSettingMapper {

    public StoreSetting toEntity(StoreSettingRequest request) {
        StoreSetting storeSetting = new StoreSetting();
        applyStoreSettingFields(storeSetting, request);
        return storeSetting;
    }

    public StoreSettingResponse toResponse(StoreSetting storeSetting) {
        return StoreSettingResponse
                .builder()
                .id(storeSetting.getId())
                .name(storeSetting.getName())
                .supportEmail(storeSetting.getSupportEmail())
                .phone(storeSetting.getPhone())
                .address(storeSetting.getAddress())
                .currency(storeSetting.getCurrency())
                .timeZone(storeSetting.getTimeZone())
                .lowStockAlert(storeSetting.getLowStockAlert())
                .freeShippingMinimum(storeSetting.getFreeShippingMinimum())
                .taxRate(storeSetting.getTaxRate())
                .notificationWeeklyReport(storeSetting.getNotificationWeeklyReport())
                .notificationNewOrders(storeSetting.getNotificationNewOrders())
                .notificationLowStock(storeSetting.getNotificationLowStock())
                .paymentCod(storeSetting.getPaymentCod())
                .paymentKhqr(storeSetting.getPaymentKhqr())
                .paymentCard(storeSetting.getPaymentCard())
                .build();
    }

    public void applyStoreSettingFields(StoreSetting setting, StoreSettingRequest request) {
        setting.setName(request.getName());
        setting.setSupportEmail(request.getSupportEmail());
        setting.setPhone(request.getPhone());
        setting.setAddress(request.getAddress());

        setting.setCurrency(request.getCurrency());
        setting.setTimeZone(request.getTimeZone());

        setting.setLowStockAlert(request.getLowStockAlert());
        setting.setFreeShippingMinimum(request.getFreeShippingMinimum());
        setting.setTaxRate(request.getTaxRate());

        setting.setNotificationNewOrders(request.getNotificationNewOrders());
        setting.setNotificationLowStock(request.getNotificationLowStock());
        setting.setNotificationWeeklyReport(request.getNotificationWeeklyReport());

        setting.setPaymentCod(request.getPaymentCod());
        setting.setPaymentKhqr(request.getPaymentKhqr());
        setting.setPaymentCard(request.getPaymentCard());

    }


}
