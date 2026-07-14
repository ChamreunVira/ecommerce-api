package com.kh.vira_dev.ecommerceapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
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

//    @JsonProperty("data")
//    public ResponseData data;
//
//    @Getter
//    @Setter
//    public static class ResponseData {
//
//        @JsonProperty("hash")
//        private String hash;
//
//        @JsonProperty("fromAccountId")
//        private String fromAccountId;
//
//        @JsonProperty("toAccountId")
//        private String toAccountId;
//
//        @JsonProperty("currency")
//        private String currency;
//
//        @JsonProperty("amount")
//        private String amount;
//
//        @JsonProperty("description")
//        private String description;
//
//        @JsonProperty("createdDateMs")
//        private Float createdDateMs;
//
//        @JsonProperty("acknowledgedDateMs")
//        private Float acknowledgedDateMs;
//
//    }


//    {
//            "responseCode": 0,
//            "responseMessage": "Getting transaction successfully.",
//            "errorCode": null,
//            "data": {
//                "hash": "e40a....",
//                "fromAccountId": "developer@cmcb",
//                "toAccountId": "developer@devb",
//                "currency": "USD",
//                "amount": 1.0,
//                "description": "",
//                "createdDateMs": 1605774370608.0,
//                "acknowledgedDateMs": 1605774422421.0
//          }
//    }
}
