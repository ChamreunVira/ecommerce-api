package com.kh.vira_dev.ecommerceapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerRequest {

    @NotBlank(message = "title is required.")
    private String title;

    private String subtitle;

    private MultipartFile image;

    private String buttonLabel;

    private String buttonLink;

    private Boolean active;

    private Integer sortOrder;
}
