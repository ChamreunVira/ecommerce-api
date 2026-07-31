package com.kh.vira_dev.ecommerceapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    @JsonProperty("responseCode")
    private Integer responseCode;

    @JsonProperty("responseMessage")
    private String responseMessage;

    @JsonProperty("errorCode")
    private Integer errorCode;

    @JsonProperty("data")
    private Object data;

}
