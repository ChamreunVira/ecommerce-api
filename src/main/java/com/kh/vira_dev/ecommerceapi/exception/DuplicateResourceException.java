package com.kh.vira_dev.ecommerceapi.exception;

public class DuplicateResourceException extends IllegalArgumentException {
    public DuplicateResourceException(String resource) {
        super(String.format("%s is already exist." , resource));
    }
}
