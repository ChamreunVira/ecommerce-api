package com.kh.vira_dev.ecommerceapi.dto.request;

import com.kh.vira_dev.ecommerceapi.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {

    @NotNull(message = "shipping address id is required.")
    private short shippingAddressId;

    private String couponCode;

    @NotNull(message = "payment method is required.")
    private PaymentMethod paymentMethod;

    @Size(max = 255 , message = "not must not exceed 255 characters.")
    private String note;


}
