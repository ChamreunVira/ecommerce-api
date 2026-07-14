package com.kh.vira_dev.ecommerceapi.dto.response;

import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionResponse {

    private Long id;

    private String code;

    private PromotionType type;

    private BigDecimal value;

    private BigDecimal minimumOrder;

    private Integer usageCount;

    private Integer usageLimit;

    private PromotionStatus status;

    private LocalDateTime startAt;

    private LocalDateTime expiryAt;

}
