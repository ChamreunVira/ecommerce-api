package com.kh.vira_dev.ecommerceapi.service;

import com.kh.vira_dev.ecommerceapi.dto.request.BakongApiRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.BakongApiResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CheckTransactionResponse;
import kh.gov.nbc.bakong_khqr.model.KHQRGenerateDeepLinkResponse;

import java.util.Map;

public interface BakongService {

    BakongApiResponse generateQrCode(BakongApiRequest request);

    KHQRGenerateDeepLinkResponse generateQrCodeDeepLink();

    CheckTransactionResponse checkTransactionByMd5(String md5);

}
