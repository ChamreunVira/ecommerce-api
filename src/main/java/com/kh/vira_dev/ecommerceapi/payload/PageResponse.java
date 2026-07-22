package com.kh.vira_dev.ecommerceapi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private List<T> content;
    private int number;
    private int size;
    private int totalPage;
    private long totalElement;
    private boolean hasPrevious;
    private boolean hasNext;
    private LocalDate timestamp;

    public static <T> PageResponse<T> success(HttpStatus status, String message , Page<T> data) {
        return builderMultipleResponse(status, message, data);
    }

    public static <T> PageResponse<T> success(String message , Page<T> data) {
        return builderMultipleResponse(HttpStatus.OK, message, data);
    }

    private static <T> PageResponse<T> builderMultipleResponse(HttpStatus status, String message , Page<T> data) {
        return PageResponse.<T>builder()
                .success(true)
                .status(status.value())
                .message(message)
                .content(data.getContent())
                .number(data.getNumber() + 1)
                .size(data.getSize())
                .totalPage(data.getTotalPages())
                .totalElement(data.getTotalElements())
                .hasPrevious(data.hasPrevious())
                .hasNext(data.hasNext())
                .timestamp(LocalDate.now())
                .build();
    }
}
