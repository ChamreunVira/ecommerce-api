package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BakongApiResponse {

    private String qrString;

    private String md5;

}
