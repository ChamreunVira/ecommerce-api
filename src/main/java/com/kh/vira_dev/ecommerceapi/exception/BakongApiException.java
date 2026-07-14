package com.kh.vira_dev.ecommerceapi.exception;

public class BakongApiException extends RuntimeException {

    public BakongApiException(int errorCode, String message) {
        super(message);
    }
}
