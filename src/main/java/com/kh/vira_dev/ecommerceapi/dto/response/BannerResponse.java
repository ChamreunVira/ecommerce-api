package com.kh.vira_dev.ecommerceapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String imageUrl;
    private String buttonLabel;
    private String buttonLink;
    private Boolean active;
    private Integer sortOrder;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
