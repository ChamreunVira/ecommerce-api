package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.config.BakongConfig;
import com.kh.vira_dev.ecommerceapi.dto.request.BakongApiRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.BakongApiResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CheckTransactionResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.TransactionResponse;
import com.kh.vira_dev.ecommerceapi.exception.BakongApiException;
import com.kh.vira_dev.ecommerceapi.service.BakongService;
import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRGenerateDeepLinkResponse;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BakongServiceImpl extends BakongConfig implements BakongService {

    private final RestTemplate restTemplate;
    private static final String BAKONG_CHECK_MD5_ENDPOIN = "/check_transaction_by_md5";

    @Override
    public BakongApiResponse generateQrCode(BakongApiRequest request) {
        IndividualInfo individualInfo = builderIndividualInfo(request);
        KHQRResponse<KHQRData> khqrResponse = BakongKHQR.generateIndividual(individualInfo);
        if(khqrResponse.getKHQRStatus().getCode() == 0) {
            var responseData = khqrResponse.getData();
            BakongApiResponse bakongApiResponse = new BakongApiResponse();
            bakongApiResponse.setQrString(responseData.getQr());
            bakongApiResponse.setMd5(responseData.getMd5());
            return bakongApiResponse;
        }

        throw new BakongApiException(khqrResponse.getKHQRStatus().getCode() , "Error cannot generate QR code");
    }

    @Override
    public KHQRGenerateDeepLinkResponse generateQrCodeDeepLink() {
        return null;
    }

    @Override
    public CheckTransactionResponse checkTransactionByMd5(String md5) {

        if(md5 == null) {
            throw new BakongApiException(1 , "Md5 not found.");
        }

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());

        //body
        Map<String, String> requestBody = Map.of("md5", md5);
        HttpEntity<Map<String, String>> body = new HttpEntity<>(requestBody, headers);

        //requst to bakong
        ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(getBAKONG_URL() + BAKONG_CHECK_MD5_ENDPOIN, body, TransactionResponse.class);
        TransactionResponse responseBody = response.getBody();
        if (responseBody == null) {
            throw new BakongApiException(1 , "Empty response from Bakong.");
        }

        return CheckTransactionResponse.builder()
                .statusCode(responseBody.getResponseCode() != null ? responseBody.getResponseCode() : -1)
                .errorCode(responseBody.getErrorCode() != null ? responseBody.getErrorCode() : -1)
                .data(responseBody.getData())
                .build();

    }

    private IndividualInfo builderIndividualInfo(BakongApiRequest request) {
        IndividualInfo info = new IndividualInfo();
        info.setBakongAccountId(getBankAccountId());
        info.setMerchantName(getMerchantName());
        info.setMerchantCity(getMerchantCity());
        info.setCurrency(request.getCurrency());
        info.setMobileNumber(getMobileNumber());
        info.setAmount(request.getAmount());
        info.setBillNumber(request.getTransactionId());
        info.setAcquiringBank("Bakong");
        info.setPurposeOfTransaction("Buy something");
        info.setMerchantCityAlternateLanguage("KM");
        info.setMerchantAlternateLanguagePreference("KM");
        info.setMerchantNameAlternateLanguage("KM");
        info.setStoreLabel("Vira Dev");
        info.setTerminalLabel("CASHIER");
        info.setExpirationTimestamp(System.currentTimeMillis() + 15 * 60 * 1000);
        return info;
    }
}
