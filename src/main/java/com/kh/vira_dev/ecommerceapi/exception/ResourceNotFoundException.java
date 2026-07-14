package com.kh.vira_dev.ecommerceapi.exception;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends NoSuchElementException {
    public ResourceNotFoundException(String resource) {
        super(String.format("%s is not found." , resource));
    }
}
