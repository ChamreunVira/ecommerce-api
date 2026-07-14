package com.kh.vira_dev.ecommerceapi.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BakongConfig {

    private final String BAKONG_URL = "https://api-bakong.nbc.gov.kh/v1";

    @Value("${bakong.token}")
    private String accessToken;

    @Value("${bakong.bankAcountId}")
    private String bankAccountId;

    @Value("${bakong.merchantName}")
    private String merchantName;

    @Value("${bakong.merchantCity}")
    private String merchantCity;

    @Value("${bakong.mobileNumber}")
    private String mobileNumber;

}
