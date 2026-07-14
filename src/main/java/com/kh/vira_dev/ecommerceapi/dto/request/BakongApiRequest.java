package com.kh.vira_dev.ecommerceapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BakongApiRequest {

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("currency")
    private KHQRCurrency currency;

}
