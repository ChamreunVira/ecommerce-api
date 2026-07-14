package com.kh.vira_dev.ecommerceapi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean success;

    private int status;

    private String message;

    private T data;

    private LocalDateTime timestamp;


    public static <T> ApiResponse<T> success(String message) {
        return responseBuilder(HttpStatus.OK.value() , message, null);
    }

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return responseBuilder(status, message, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return responseBuilder(HttpStatus.OK.value(), message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return responseBuilder(HttpStatus.OK.value(), "Successfully.", data);
    }

    private static <T> ApiResponse<T> responseBuilder(int status , String message , T data) {
        return ApiResponse
                .<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
