package com.kh.vira_dev.ecommerceapi.dto.request;

import com.kh.vira_dev.ecommerceapi.enums.PromotionStatus;
import com.kh.vira_dev.ecommerceapi.enums.PromotionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PromotionRequest {

    @NotBlank(message = "code is required.")
    private String code;

    @NotNull(message = "type is required.")
    private PromotionType type;

    @DecimalMin(value = "0.0", message = "value must be >= 0.")
    private BigDecimal value;

    @DecimalMin(value = "0.0", message = "minimumOrder must be >= 0.")
    private BigDecimal minimumOrder;

    @Min(value = 0, message = "usageLimit must be >= 0.")
    private Integer usageLimit;

    @NotNull(message = "status is required.")
    private PromotionStatus status;

    private LocalDateTime startAt;

    private LocalDateTime expiryAt;

}
