package com.kh.vira_dev.ecommerceapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class CheckTransactionResponse {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errorCode")
    private int errorCode;

    @JsonProperty("data")
    Object data;

}
