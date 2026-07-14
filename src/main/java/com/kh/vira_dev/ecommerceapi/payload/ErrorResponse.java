package com.kh.vira_dev.ecommerceapi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse<T> {

    private boolean success;

    private int status;

    private String message;

    private T errors;

    private LocalDateTime timestamp;


    public static <T> ErrorResponse<T> error(String message) {
        return responseBuilder(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <T> ErrorResponse<T> error(String message, T errors) {
        return responseBuilder(HttpStatus.BAD_REQUEST.value(), message, errors);
    }


    private static <T> ErrorResponse<T> responseBuilder(int status, String message, T errors) {
        return ErrorResponse
                .<T>builder()
                .status(status)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
